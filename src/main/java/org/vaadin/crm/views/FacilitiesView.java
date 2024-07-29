package org.vaadin.crm.views;

import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.applayout.DrawerToggle;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.confirmdialog.ConfirmDialog;
import com.vaadin.flow.component.dependency.Uses;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.Scroller;
import com.vaadin.flow.component.sidenav.SideNav;
import com.vaadin.flow.component.sidenav.SideNavItem;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.provider.DataKeyMapper;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.data.renderer.LocalDateTimeRenderer;
import com.vaadin.flow.data.renderer.Renderer;
import com.vaadin.flow.data.renderer.TextRenderer;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.dom.Element;
import com.vaadin.flow.function.SerializableBiConsumer;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.theme.lumo.LumoUtility;
import org.vaadin.crm.entities.Facility;
import org.vaadin.crm.services.CrmService;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;

@Route("facilities")

public class FacilitiesView extends AppLayout {
    Grid<Facility> grid = new Grid<>(Facility.class);
    TextField filterText = new TextField();
    Button but1 = new Button("Add file");
    Long id;

    FacilityDialog facilityDialog;

    CrmService service;

    public FacilitiesView(CrmService service) {
        this.service = service;
        DrawerToggle toggle = new DrawerToggle();
        H1 title = new H1("Customers");
        title.getStyle().set("font-size", "var(--lumo-font-size-l)")
                .set("margin", "10");
        SideNav nav = getTabs();
        Scroller scroller = new Scroller(nav);
        scroller.setClassName(LumoUtility.Padding.SMALL);
        Button addContactButton = new Button("Добавить контакт");
        addContactButton.addClickListener(click -> addFacility());

        but1.addClickListener(click -> {
            id = grid.asSingleSelect().getValue().getId();
            File path = new File("/facility/"+id);
            Desktop desktop = null;
            desktop = Desktop.getDesktop();
            try {
                desktop.open(path.getAbsoluteFile());
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        addToDrawer(scroller);
        addToNavbar(toggle, title, filterText, but1, addContactButton);

        filterText.setPlaceholder("Сортировать по алфавиту...");
        filterText.setClearButtonVisible(true);
        filterText.setValueChangeMode(ValueChangeMode.LAZY);
        filterText.addValueChangeListener(e -> updateList());
        setPrimarySection(Section.DRAWER);
        configureGrid();
        setContent(grid);
        updateList();
    }

    private SideNav getTabs() {
        SideNav nav = new SideNav();
        nav.addItem(new SideNavItem("Объекты", "/facilities",
                        VaadinIcon.CART.create()),
                new SideNavItem("Компании", "/customers",
                        VaadinIcon.USER_HEART.create()),
                new SideNavItem("Documents", "/documents",
                        VaadinIcon.RECORDS.create()));
        return nav;
    }

    private void configureGrid() {
        grid.addClassName("facilities-grid");
        grid.setSizeFull();
        grid.setColumns("dateTime", "facilityName", "area", "address", "price", "owner", "facilityReadiness", "description", "contactLinks");
        grid.getColumns().get(0).setHeader("Дата").setWidth("90px").setResizable(true);


        grid.getColumns().get(1).setHeader("Название объекта");
        grid.getColumns().get(2).setHeader("Площадь, м2").setWidth("55px");
        grid.getColumns().get(3).setHeader("Адрес");
        grid.getColumns().get(4).setHeader("Стоимость");
        grid.getColumns().get(5).setHeader("Арендатор, коммерческие условия");
        grid.getColumns().get(6).setHeader("Стадия готовности");
        grid.getColumns().get(7).setHeader("Описание объекта").setHeaderPartName("header-description").setRenderer(createTextAreaRenderer())
                .setWidth("250px").setResizable(true);
        grid.getColumns().get(8).setHeader("Контакты/ссылки");

        grid.addColumn(createStatusComponentRenderer()).setHeader("Статус");

        //grid.addColumn(facility -> facility.getStatus().getName()).setHeader("Статус");
        //grid.addColumn(Company::getComments).setHeader("Комментарии");

        //TODO
        //grid.getColumns().forEach(col -> col.setAutoWidth(true));
        grid.getColumns().get(0).setRenderer(new LocalDateTimeRenderer<>(
                        Facility::getDateTime,
                        () -> DateTimeFormatter.ofLocalizedDateTime(
                                FormatStyle.SHORT,
                                FormatStyle.SHORT)));

        // установить распределение текста в ячейке по высоте
        grid.addThemeVariants(GridVariant.LUMO_WRAP_CELL_CONTENT);
        //grid.addThemeVariants(GridVariant.LUMO_COLUMN_BORDERS);

        grid.addItemDoubleClickListener(e -> {
            facilityDialog = new FacilityDialog(service, grid.asSingleSelect().getValue());
            FacilityForm.id = grid.asSingleSelect().getValue().getId();
            addToNavbar(facilityDialog);
        });
    }

    // Убрал модификатор static, т.к. падало SerializableException
    private final SerializableBiConsumer<Span, Facility> statusComponentUpdater = (
            span, facility) -> {
        boolean isCustomer = "Customer".equals(facility.getStatus().getName());
        boolean isImported = "Imported lead".equals(facility.getStatus().getName());
        boolean isContacted = "Contacted".equals(facility.getStatus().getName());
        boolean notContacted = "Not contacted".equals(facility.getStatus().getName());
        boolean isLost = "Closed (lost)".equals(facility.getStatus().getName());

        String theme = "";
        if(isCustomer) theme = "badge success";
        if(isImported) theme = "badge";
        if(isContacted) theme = "badge";
        if(notContacted) theme = "badge contrast";
        if(isLost) theme = "badge error";
        span.getElement().setAttribute("theme", theme);
        span.setText(facility.getStatus().getName());

        /*
        String theme = String.format("badge %s",
                isCustomer ? "success" : "error");
        span.getElement().setAttribute("theme", theme);
        span.setText(facility.getStatus().getName());
        */
    };

    private ComponentRenderer<Span, Facility> createStatusComponentRenderer() {
        return new ComponentRenderer<>(Span::new, statusComponentUpdater);
    }

    private final SerializableBiConsumer<TextArea, Facility> textAreaRenderer = (
            textArea, facility) -> {
        textArea.setMaxHeight("77px");
        textArea.setWidth(("277px"));
        textArea.setReadOnly(true);
        if(facility.getDescription() != null) textArea.setValue(facility.getDescription());
        else if(facility.getDescription() == null) textArea.setValue("добавить описание");
    };

    private ComponentRenderer<TextArea, Facility> createTextAreaRenderer() {
        return new ComponentRenderer<>(TextArea::new, textAreaRenderer);
    }

    private void saveContact(ContactForm.SaveEvent event) {
        service.saveContact(event.getCompany());
    }

    private void deleteContact(ContactForm.DeleteEvent event) {
        service.deleteContact(event.getCompany());
    }

    private void addFacility() {
        grid.asSingleSelect().clear();
        facilityDialog = new FacilityDialog(service);
        addToNavbar(facilityDialog);
    }

    private void updateList() {

        grid.setItems(service.findAllFacilities(filterText.getValue()));
    }
}
