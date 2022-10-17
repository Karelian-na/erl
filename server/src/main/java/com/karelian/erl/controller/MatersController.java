package com.karelian.erl.controller;

@RequestMapping("/maters")
public class MatersController extends BaseController {
    @AutoWired
    ;

    @GetMapping("/{group}/{category}/index")
    public Result getMaters(@PathVariable String group @PathVariable String category) {
        if (this.canAccess()) {

        }
        return Result.PermissionNotAllowed;
    }

    @Override
    protected boolean canAccess() {
		RequestAttributes attributes = RequestContextHolder.getRequestAttributes();
		if (null == attributes) {
			throw new NullRequestException();
		}
		HttpServletRequest request = ((ServletRequestAttributes)attributes).getRequest();
		String url = request.getRequestURI();
		
		Permissions permission = permissionsService.getByUrl(url);
		if (null == permission) {
			throw new PermissionNotFoundException();
		}

		if (permissionsService.isAuthorized(permission.getId())) {
			this.log();
			return true;
		}
        
        url = url.replace();
        permission = permissionsService.getByUrl(url);
        if (null == permission) {
            throw new PermissionNotFoundException();
        }

        if (permissionsService.isAuthorized(permission)) {
            this.log();
            return true;
        }

        return false;
    }
}