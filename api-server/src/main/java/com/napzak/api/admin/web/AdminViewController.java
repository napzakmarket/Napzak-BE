package com.napzak.api.admin.web;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.napzak.api.admin.dto.response.AdminDashboardResponse;
import com.napzak.api.admin.dto.response.AdminLoginResponse;
import com.napzak.api.admin.service.AdminDashboardService;
import com.napzak.api.admin.service.AdminLoginService;
import com.napzak.api.admin.service.AdminService;
import com.napzak.common.exception.NapzakException;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminViewController {

	private static final int COOKIE_MAX_AGE_SECONDS = 60 * 60; // 1시간 (accessToken 수명에 맞춰 조정)

	private final AdminLoginService adminLoginService;
	private final AdminDashboardService adminDashboardService;

	@GetMapping("/login")
	public String loginPage() {
		return "admin/login";
	}

	@PostMapping("/login")
	public String login(
		@RequestParam String loginId,
		@RequestParam String password,
		HttpServletResponse response,
		RedirectAttributes redirectAttributes
	) {
		try {
			AdminLoginResponse tokens = adminLoginService.login(loginId, password);
			response.addCookie(buildTokenCookie(tokens.accessToken(), COOKIE_MAX_AGE_SECONDS));
			return "redirect:/admin";
		} catch (NapzakException e) {
			redirectAttributes.addFlashAttribute("error", e.getMessage());
			return "redirect:/admin/login";
		}
	}

	@GetMapping({"", "/"})
	public String dashboard(Model model) {
		AdminDashboardResponse dashboard = adminDashboardService.getDashboard();
		model.addAttribute("dashboard", dashboard);
		return "admin/dashboard";
	}

	@GetMapping("/users")
	public String users(
		@RequestParam(name = "page", defaultValue = "0") int page,
		Model model
	) {
		AdminUserListResponse userList = adminService.getUserList(page);
		model.addAttribute("userList", userList);
		return "admin/users";
	}

	@PostMapping("/users/report")
	public String reportUser(
		@RequestParam("storeId") Long storeId,
		@RequestParam("mode") String mode,
		@RequestParam(name = "page", defaultValue = "0") int page,
		RedirectAttributes redirectAttributes
	) {
		try {
			if ("approve".equals(mode)) {
				adminService.reportAndApproveStore(storeId);
				redirectAttributes.addFlashAttribute("toastSuccess", "신고 처리 및 승인이 완료되었습니다");
			} else {
				adminService.reportStore(storeId);
				redirectAttributes.addFlashAttribute("toastSuccess", "신고 발행이 완료되었습니다");
			}
		} catch (NapzakException e) {
			redirectAttributes.addFlashAttribute("toastError", e.getMessage());
		}
		return "redirect:/admin/users?page=" + page;
	}

	@PostMapping("/users/approve")
	public String approveUserReport(
		@RequestParam("storeId") Long storeId,
		@RequestParam("reportId") Long reportId,
		@RequestParam(name = "page", defaultValue = "0") int page,
		RedirectAttributes redirectAttributes
	) {
		try {
			adminService.approveExistingReport(storeId, reportId);
			redirectAttributes.addFlashAttribute("toastSuccess", "신고 승인이 완료되었습니다");
		} catch (NapzakException e) {
			redirectAttributes.addFlashAttribute("toastError", e.getMessage());
		}
		return "redirect:/admin/users?page=" + page;
	}
	@PostMapping("/logout")
	public String logout(HttpServletResponse response) {
		response.addCookie(buildTokenCookie("", 0));
		return "redirect:/admin/login";
	}

	private Cookie buildTokenCookie(String value, int maxAge) {
		Cookie cookie = new Cookie(AdminAuthInterceptor.ADMIN_TOKEN_COOKIE, value);
		cookie.setHttpOnly(true);
		cookie.setPath("/");
		cookie.setMaxAge(maxAge);
		// cookie.setSecure(true); // HTTPS 운영 환경에서 활성화
		return cookie;
	}
}
