package org.vaadin.crm.services;

import org.vaadin.crm.entities.Company;
import org.vaadin.crm.entities.Contact;
import org.vaadin.crm.entities.Status;


import org.vaadin.crm.entities.Task;
import org.vaadin.crm.repositories.CompanyRepository;
import org.vaadin.crm.repositories.ContactRepository;
import org.vaadin.crm.repositories.StatusRepository;
import org.springframework.stereotype.Service;
import org.vaadin.crm.repositories.TaskRepository;

import java.util.List;

@Service
public class CrmService {

    private final ContactRepository contactRepository;
    private final CompanyRepository companyRepository;
    private final StatusRepository statusRepository;
    private final TaskRepository taskRepository;

    public CrmService(ContactRepository contactRepository,
                      CompanyRepository companyRepository,
                      StatusRepository statusRepository,
                      TaskRepository taskRepository) {
        this.contactRepository = contactRepository;
        this.companyRepository = companyRepository;
        this.statusRepository = statusRepository;
        this.taskRepository = taskRepository;
    }

    public List<Contact> findAllContacts(String stringFilter) {
        if (stringFilter == null || stringFilter.isEmpty()) {
            return contactRepository.findAll();
        } else {
            return contactRepository.search(stringFilter);
        }
    }

    public long countContacts() {
        return contactRepository.count();
    }

    public void deleteContact(Contact contact) {
        contactRepository.delete(contact);
    }

    public void saveContact(Contact contact) {
        if (contact == null) {
            System.err.println("Contact is null. Are you sure you have connected your form to the application?");
            return;
        }
        contactRepository.save(contact);
    }

    public List<Company> findAllCompanies() {
        return companyRepository.findAll();
    }

    public List<Status> findAllStatuses(){
        return statusRepository.findAll();
    }

    public void saveDate(Task task) {
        if (task == null) {
            System.err.println("Contact is null. Are you sure you have connected your form to the application?");
            return;
        }
        taskRepository.save(task);
    }
}