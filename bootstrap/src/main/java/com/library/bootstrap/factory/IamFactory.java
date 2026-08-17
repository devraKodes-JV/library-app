package com.library.bootstrap.factory;

import org.hibernate.SessionFactory;

import com.library.security.SecurityFactory;
import com.library.security.service.SecurityAuditService;
import com.library.iam.application.service.login.LoadUserByUsernameUseCase;
import com.library.iam.application.service.module.ListModulesUseCase;
import com.library.iam.application.service.navigation.BuildNavigationUseCase;
import com.library.iam.application.service.password.PasswordOperationsUseCase;
import com.library.iam.application.service.permission.ListPermissionsFlatUseCase;
import com.library.iam.application.service.permission.ListPermissionsGroupedByModuleUseCase;
import com.library.iam.application.service.role.CreateRoleUseCase;
import com.library.iam.application.service.role.DeleteRoleUseCase;
import com.library.iam.application.service.role.GetRoleDetailUseCase;
import com.library.iam.application.service.role.ListRolesUseCase;
import com.library.iam.application.service.role.UpdateRoleUseCase;
import com.library.iam.application.service.user.CreateUserUseCase;
import com.library.iam.application.service.user.DeleteUserUseCase;
import com.library.iam.application.service.user.DisableUserUseCase;
import com.library.iam.application.service.user.GetUserUseCase;
import com.library.iam.application.service.user.ListActiveUsersUseCase;
import com.library.iam.application.service.user.ListInactiveUsersUseCase;
import com.library.iam.application.service.user.ReinstateUserUseCase;
import com.library.iam.application.service.user.UpdateUserUseCase;
import com.library.iam.application.validation.CreateRoleCommandValidator;
import com.library.iam.application.validation.CreateUserCommandValidator;
import com.library.iam.application.validation.UpdateRoleCommandValidator;
import com.library.iam.application.validation.UpdateUserCommandValidator;
import com.library.iam.domain.port.out.ModulePort;
import com.library.iam.domain.port.out.NotificationService;
import com.library.iam.domain.port.out.PasswordHasher;
import com.library.iam.domain.port.out.PermissionPort;
import com.library.iam.domain.port.out.RolePort;
import com.library.iam.domain.port.out.UserPort;
import com.library.iam.infrastructure.notification.SseNotificationService;
import com.library.iam.infrastructure.persistence.adapter.ModulePersistenceAdapter;
import com.library.iam.infrastructure.persistence.adapter.PermissionPersistenceAdapter;
import com.library.iam.infrastructure.persistence.adapter.RolePersistenceAdapter;
import com.library.iam.infrastructure.persistence.adapter.UserPersistenceAdapter;
import com.library.iam.infrastructure.persistence.repository.hibernate.HibernateModuleRepository;
import com.library.iam.infrastructure.persistence.repository.hibernate.HibernatePermissionRepository;
import com.library.iam.infrastructure.persistence.repository.hibernate.HibernateRoleRepository;
import com.library.iam.infrastructure.persistence.repository.hibernate.HibernateUserRepository;
import com.library.iam.infrastructure.security.BouncyCastleArgon2PasswordHasher;
import com.library.iam.infrastructure.web.IamRoutes;
import com.library.iam.infrastructure.web.IamWebControllerContext;
import com.library.iam.infrastructure.web.controller.dashboard.ShowDashboardController;
import com.library.iam.infrastructure.web.controller.login.LoginController;
import com.library.iam.infrastructure.web.controller.login.LogoutController;
import com.library.iam.infrastructure.web.controller.module.ListModulesController;
import com.library.iam.infrastructure.web.controller.permission.ListPermissionsController;
import com.library.iam.infrastructure.web.controller.role.CreateRoleController;
import com.library.iam.infrastructure.web.controller.role.DeleteRoleController;
import com.library.iam.infrastructure.web.controller.role.ListRolesController;
import com.library.iam.infrastructure.web.controller.role.UpdateRoleController;
import com.library.iam.infrastructure.web.controller.user.CreateUserController;
import com.library.iam.infrastructure.web.controller.user.DeleteUserController;
import com.library.iam.infrastructure.web.controller.user.DisableUserController;
import com.library.iam.infrastructure.web.controller.user.ListUsersController;
import com.library.iam.infrastructure.web.controller.user.ReinstateUserController;
import com.library.iam.infrastructure.web.controller.user.UpdateUserController;
import com.library.kernel.web.WebControllerContext;

import io.javalin.config.JavalinConfig;

public final class IamFactory {

    private IamFactory() {
    }

    public static WebControllerContext register(JavalinConfig config, SessionFactory sessionFactory) {
        UserPort userPort = new UserPersistenceAdapter(new HibernateUserRepository(sessionFactory));
        RolePort rolePort = new RolePersistenceAdapter(new HibernateRoleRepository(sessionFactory));
        PermissionPort permissionPort = new PermissionPersistenceAdapter(new HibernatePermissionRepository(sessionFactory));
        ModulePort modulePort = new ModulePersistenceAdapter(new HibernateModuleRepository(sessionFactory));

        NotificationService notificationService = new SseNotificationService();
        PasswordHasher passwordHasher = new BouncyCastleArgon2PasswordHasher();

        PasswordOperationsUseCase passwordOperationsUseCase = new PasswordOperationsUseCase(passwordHasher);
        LoadUserByUsernameUseCase loadUserByUsernameUseCase = new LoadUserByUsernameUseCase(userPort);
        BuildNavigationUseCase buildNavigationUseCase = new BuildNavigationUseCase();
        ListPermissionsFlatUseCase listPermissionsFlatUseCase = new ListPermissionsFlatUseCase(permissionPort);
        ListPermissionsGroupedByModuleUseCase listPermissionsGroupedByModuleUseCase = new ListPermissionsGroupedByModuleUseCase(permissionPort);
        ListModulesUseCase listModulesUseCase = new ListModulesUseCase(modulePort);

        CreateUserCommandValidator createUserCommandValidator = new CreateUserCommandValidator();
        UpdateUserCommandValidator updateUserCommandValidator = new UpdateUserCommandValidator();
        CreateRoleCommandValidator createRoleCommandValidator = new CreateRoleCommandValidator();
        UpdateRoleCommandValidator updateRoleCommandValidator = new UpdateRoleCommandValidator();

        ListRolesUseCase listRolesUseCase = new ListRolesUseCase(rolePort);
        GetRoleDetailUseCase getRoleDetailUseCase = new GetRoleDetailUseCase(rolePort);
        CreateRoleUseCase createRoleUseCase = new CreateRoleUseCase(rolePort, permissionPort, notificationService, createRoleCommandValidator);
        UpdateRoleUseCase updateRoleUseCase = new UpdateRoleUseCase(rolePort, permissionPort, notificationService, updateRoleCommandValidator);
        DeleteRoleUseCase deleteRoleUseCase = new DeleteRoleUseCase(rolePort, notificationService);

        ListActiveUsersUseCase listActiveUsersUseCase = new ListActiveUsersUseCase(userPort);
        ListInactiveUsersUseCase listInactiveUsersUseCase = new ListInactiveUsersUseCase(userPort);
        GetUserUseCase getUserUseCase = new GetUserUseCase(userPort);
        CreateUserUseCase createUserUseCase = new CreateUserUseCase(userPort, rolePort, notificationService, passwordHasher, createUserCommandValidator);
        UpdateUserUseCase updateUserUseCase = new UpdateUserUseCase(userPort, rolePort, notificationService, updateUserCommandValidator);
        DisableUserUseCase disableUserUseCase = new DisableUserUseCase(userPort, notificationService);
        DeleteUserUseCase deleteUserUseCase = new DeleteUserUseCase(userPort, notificationService);
        ReinstateUserUseCase reinstateUserUseCase = new ReinstateUserUseCase(userPort, notificationService);

        SecurityAuditService auditService = SecurityFactory.register(config, sessionFactory);

        WebControllerContext webContext = new IamWebControllerContext(buildNavigationUseCase::execute);

        LoginController loginController = new LoginController(loadUserByUsernameUseCase, passwordOperationsUseCase, auditService);
        LogoutController logoutController = new LogoutController(auditService);
        ShowDashboardController showDashboardController = new ShowDashboardController(buildNavigationUseCase);
        ListRolesController listRolesController = new ListRolesController(listRolesUseCase, webContext);
        CreateRoleController createRoleController = new CreateRoleController(
                createRoleUseCase,
                listPermissionsGroupedByModuleUseCase,
                webContext);
        UpdateRoleController updateRoleController = new UpdateRoleController(
                getRoleDetailUseCase,
                updateRoleUseCase,
                listPermissionsGroupedByModuleUseCase,
                webContext);
        DeleteRoleController deleteRoleController = new DeleteRoleController(deleteRoleUseCase, webContext);
        ListUsersController listUsersController = new ListUsersController(
                listActiveUsersUseCase,
                listInactiveUsersUseCase,
                listRolesUseCase,
                webContext);
        CreateUserController createUserController = new CreateUserController(
                createUserUseCase,
                listRolesUseCase,
                webContext);
        UpdateUserController updateUserController = new UpdateUserController(
                getUserUseCase,
                updateUserUseCase,
                listRolesUseCase,
                webContext);
        DeleteUserController deleteUserController = new DeleteUserController(deleteUserUseCase, webContext);
        DisableUserController disableUserController = new DisableUserController(disableUserUseCase, webContext);
        ReinstateUserController reinstateUserController = new ReinstateUserController(reinstateUserUseCase, webContext);
        ListPermissionsController listPermissionsController = new ListPermissionsController(listPermissionsFlatUseCase, webContext);
        ListModulesController listModulesController = new ListModulesController(listModulesUseCase, webContext);

        IamRoutes.register(config,
                loginController,
                logoutController,
                showDashboardController,
                listRolesController,
                createRoleController,
                updateRoleController,
                deleteRoleController,
                listUsersController,
                createUserController,
                updateUserController,
                deleteUserController,
                disableUserController,
                reinstateUserController,
                listPermissionsController,
                listModulesController,
                (SseNotificationService) notificationService,
                auditService);

        return webContext;
    }
}
