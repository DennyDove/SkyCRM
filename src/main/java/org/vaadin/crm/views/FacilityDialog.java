package org.vaadin.crm.views;


import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.confirmdialog.ConfirmDialog;
import com.vaadin.flow.component.dependency.Uses;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.Div;
import org.vaadin.crm.entities.Company;
import org.vaadin.crm.entities.Facility;
import org.vaadin.crm.services.CrmService;

import java.time.LocalDateTime;

//@Route("facility-dialog")
public class FacilityDialog extends Div {

    public FacilityDialog(CrmService service) {
        Dialog dialog = new Dialog();
        this.addClassName("popup-window");

        FacilityForm form = new FacilityForm(service, service.findAllStatuses());
        form.setFacility(new Facility());
        form.dateTime.setValue(LocalDateTime.now().withNano(0));

        //Стили всплывающего окна
        dialog.setHeaderTitle("Создать новый контакт");
        dialog.setHeight("777px");
        dialog.setWidth("623px");
        dialog.setMaxWidth("100%");


        //VerticalLayout dialogLayout = createDialogLayout();

        dialog.add(form);
        dialog.addDialogCloseActionListener(e -> {
            if(dialog.isCloseOnOutsideClick()) {
                UI ui = this.getUI().get();
                ui.refreshCurrentRoute(true);
            }
        });

        add(dialog);

        dialog.open();

    }

    public FacilityDialog(CrmService service, Facility facility) {
        Dialog dialog = new Dialog();
        addClassName("popup-window");

        FacilityForm form = new FacilityForm(service, service.findAllStatuses());
        form.setFacility(facility);
        if(form.dateTime.isEmpty()) {
            form.dateTime.setValue(LocalDateTime.now().withNano(0));
        }

        dialog.setHeaderTitle("Редактировать данные");

        //VerticalLayout dialogLayout = createDialogLayout();

        dialog.add(form);
        dialog.addDialogCloseActionListener(e -> {
                    if(dialog.isCloseOnOutsideClick()) {
                        UI ui = this.getUI().get();
                        ui.refreshCurrentRoute(true);
                    }
        });

        add(dialog);

        dialog.open();
    }


    /*
    private static VerticalLayout createDialogLayout() {

        TextField firstNameField = new TextField("First name");
        TextField lastNameField = new TextField("Last name");

        VerticalLayout dialogLayout = new VerticalLayout(firstNameField,
                lastNameField);
        dialogLayout.setPadding(false);
        dialogLayout.setSpacing(false);
        dialogLayout.setAlignItems(FlexComponent.Alignment.STRETCH);
        dialogLayout.getStyle().set("width", "12rem").set("max-width", "100%");

        return dialogLayout;
    }

    private static Button createSaveButton(Dialog dialog) {
        Button saveButton = new Button("Add", e -> dialog.close());
        saveButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

        return saveButton;
    }
    */

}