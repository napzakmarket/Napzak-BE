package com.napzak.api.admin.web;

import java.util.Arrays;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.napzak.api.admin.dto.response.AdminChatListResponse;
import com.napzak.api.admin.dto.response.AdminDashboardResponse;
import com.napzak.api.admin.dto.response.AdminLoginResponse;
import com.napzak.api.admin.dto.response.AdminStoreReportListResponse;
import com.napzak.api.admin.dto.response.AdminUserListResponse;
import com.napzak.api.admin.service.AdminLoginService;
import com.napzak.api.admin.service.AdminService;
import com.napzak.common.auth.role.enums.Role;
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
	private final AdminService adminService;

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
		AdminDashboardResponse dashboard = adminService.getDashboard();
		model.addAttribute("dashboard", dashboard);
		return "admin/dashboard";
	}

	@GetMapping("/users")
	public String users(
		@RequestParam(name = "page", defaultValue = "0") int page,
		@RequestParam(name = "q", required = false) String q,
		Model model
	) {
		AdminUserListResponse userList = adminService.getUserList(page, q);
		model.addAttribute("userList", userList);
		model.addAttribute("roles", Arrays.stream(Role.values()).map(Enum::name).toList());
		return "admin/users";
	}

	@PostMapping("/users/role")
	public String updateUserRole(
		@RequestParam("storeId") Long storeId,
		@RequestParam("role") String role,
		@RequestParam(name = "page", defaultValue = "0") int page,
		@RequestParam(name = "q", required = false) String q,
		RedirectAttributes redirectAttributes
	) {
		try {
			adminService.updateStoreRole(storeId, role);
			redirectAttributes.addFlashAttribute("toastSuccess", "ROLE이 변경되었습니다");
		} catch (NapzakException e) {
			redirectAttributes.addFlashAttribute("toastError", e.getMessage());
		}
		return redirectToUsers(page, q, redirectAttributes);
	}

	@PostMapping("/users/report")
	public String reportUser(
		@RequestParam("storeId") Long storeId,
		@RequestParam("mode") String mode,
		@RequestParam(name = "page", defaultValue = "0") int page,
		@RequestParam(name = "q", required = false) String q,
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
		return redirectToUsers(page, q, redirectAttributes);
	}

	@PostMapping("/users/approve")
	public String approveUserReport(
		@RequestParam("storeId") Long storeId,
		@RequestParam("reportId") Long reportId,
		@RequestParam(name = "page", defaultValue = "0") int page,
		@RequestParam(name = "q", required = false) String q,
		RedirectAttributes redirectAttributes
	) {
		try {
			adminService.approveExistingReport(storeId, reportId);
			redirectAttributes.addFlashAttribute("toastSuccess", "신고 승인이 완료되었습니다");
		} catch (NapzakException e) {
			redirectAttributes.addFlashAttribute("toastError", e.getMessage());
		}
		return redirectToUsers(page, q, redirectAttributes);
	}

	private String redirectToUsers(int page, String q, RedirectAttributes redirectAttributes) {
		redirectAttributes.addAttribute("page", page);
		if (q != null && !q.isBlank()) {
			redirectAttributes.addAttribute("q", q);
		}
		return "redirect:/admin/users";
	}

	@GetMapping("/reports/store")
	public String storeReports(
		@RequestParam(name = "page", defaultValue = "0") int page,
		Model model
	) {
		AdminStoreReportListResponse reportList = adminService.getReportList(page);
		model.addAttribute("reportList", reportList);
		return "admin/reports";
	}

	@PostMapping("/reports/store/{reportId}/approve")
	public String approveStoreReport(
		@PathVariable("reportId") Long reportId,
		@RequestParam("reportedStoreId") Long reportedStoreId,
		@RequestParam(name = "page", defaultValue = "0") int page,
		RedirectAttributes redirectAttributes
	) {
		try {
			adminService.approveExistingReport(reportedStoreId, reportId);
			redirectAttributes.addFlashAttribute("toastSuccess", "신고 승인이 완료되었습니다");
		} catch (NapzakException e) {
			redirectAttributes.addFlashAttribute("toastError", e.getMessage());
		}
		return "redirect:/admin/reports/store?page=" + page;
	}

	@GetMapping("/chats")
	public String chats(
		@RequestParam(name = "page", defaultValue = "0") int page,
		@RequestParam(name = "type", required = false) String type,
		@RequestParam(name = "q", required = false) String q,
		Model model
	) {
		AdminChatListResponse chatList = adminService.getChatList(page, type, q);
		model.addAttribute("chatList", chatList);
		return "admin/chats";
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
