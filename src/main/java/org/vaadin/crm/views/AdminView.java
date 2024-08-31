package org.vaadin.crm.views;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.applayout.DrawerToggle;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.Scroller;
import com.vaadin.flow.component.sidenav.SideNav;
import com.vaadin.flow.component.sidenav.SideNavItem;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.theme.lumo.LumoUtility;
import org.vaadin.crm.services.CrmService;

@Route("admin")
// tag::snippet[]
public class AdminView extends AppLayout {

    UserDialog userDialog;

    CrmService service;

    public AdminView(CrmService service) {
        this.service = service;
        DrawerToggle toggle = new DrawerToggle();

        H1 title = new H1("Dashboard");
        title.getStyle().set("font-size", "var(--lumo-font-size-l)")
                .set("margin", "0");

        SideNav nav = getTabs();

        Scroller scroller = new Scroller(nav);
        scroller.setClassName(LumoUtility.Padding.SMALL);

        addUser.addClickListener(click -> {
            userDialog = new UserDialog(service);
            addToNavbar(userDialog);
        });

        addToDrawer(scroller);
        addToNavbar(toggle, title, addUser);

        setPrimarySection(Section.DRAWER);

        setContent(addUser);
    }
    // end::snippet[]

    private SideNav getTabs() {
        SideNav nav = new SideNav();
        nav.addItem(new SideNavItem("Dashboard", "/dashboard",
                        VaadinIcon.DASHBOARD.create()),
                new SideNavItem("Объекты", "/orders",
                        VaadinIcon.CART.create()),
                new SideNavItem("Компании", "/customers",
                        VaadinIcon.USER_HEART.create()),
                new SideNavItem("Documents", "/documents",
                        VaadinIcon.RECORDS.create()),
                new SideNavItem("Tasks", "/tasks",
                        VaadinIcon.LIST.create()));
        return nav;
    }

    private Button addUser = new Button("Создать пользователя");

    // tag::snippet[]
}
// end::snippet[]
