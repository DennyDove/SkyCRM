package org.vaadin.crm.services;

import org.vaadin.crm.entities.Company;
import org.vaadin.crm.entities.Status;


import org.vaadin.crm.repositories.CompanyRepository;
import org.vaadin.crm.repositories.StatusRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CrmService {

    private final CompanyRepository companyRepository;
    private final StatusRepository statusRepository;

    public CrmService(CompanyRepository companyRepository,
                      StatusRepository statusRepository) {
        this.companyRepository = companyRepository;
        this.statusRepository = statusRepository;
    }

    public List<Company> findAllContacts(String stringFilter) {
        if (stringFilter == null || stringFilter.isEmpty()) {
            return companyRepository.findAll();
        } else {
            return companyRepository.search(stringFilter);
        }
    }

    public long countContacts() {
        return companyRepository.count();
    }

    public void deleteContact(Company company) {
        companyRepository.delete(company);
    }

    public void saveContact(Company company) {
        if (company == null) {
            System.err.println("Contact is null. Are you sure you have connected your form to the application?");
            return;
        }
        companyRepository.save(company);
    }


    public List<Status> findAllStatuses(){
        return statusRepository.findAll();
    }


    /*public List<Task> findTasksByContactId(){
        return taskRepository.findTasksByContactId();
    }*/

}