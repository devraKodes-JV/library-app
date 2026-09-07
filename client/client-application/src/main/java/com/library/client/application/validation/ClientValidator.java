package com.library.client.application.validation;

import com.library.client.domain.model.Client;
import com.library.kernel.validation.Validator;

public class ClientValidator implements Validator<Client> {

    private static final java.util.regex.Pattern DNI_PATTERN = java.util.regex.Pattern.compile("^\\d{8}[A-Za-z]$");
    private static final java.util.regex.Pattern EMAIL_PATTERN = java.util.regex.Pattern.compile("^[\\w.-]+@[\\w.-]+\\.[a-zA-Z]{2,}$");
    private static final java.util.regex.Pattern PHONE_PATTERN = java.util.regex.Pattern.compile("^\\+?[0-9\\s-]{7,20}$");
    private static final java.util.regex.Pattern NAME_PATTERN = java.util.regex.Pattern.compile("^[a-zA-ZáéíóúÁÉÍÓÚñÑ\\s'-]{2,100}$");

    @Override
    public void validate(Client client) {
        var errors = new java.util.LinkedHashMap<String, String>();

        if (client.getDni() == null || client.getDni().isBlank()) {
            errors.put("dni", "DNI is required.");
        } else if (!DNI_PATTERN.matcher(client.getDni()).matches()) {
            errors.put("dni", "DNI must be 8 digits followed by a letter.");
        }

        if (client.getFullName() == null || client.getFullName().isBlank()) {
            errors.put("fullName", "Full name is required.");
        } else if (!NAME_PATTERN.matcher(client.getFullName()).matches()) {
            errors.put("fullName", "Full name must contain only letters, spaces, hyphens or apostrophes.");
        }

        if (client.getEmail() != null && !client.getEmail().isBlank()) {
            if (!EMAIL_PATTERN.matcher(client.getEmail()).matches()) {
                errors.put("email", "Email format is invalid.");
            }
        }

        if (client.getPhone() != null && !client.getPhone().isBlank()) {
            if (!PHONE_PATTERN.matcher(client.getPhone()).matches()) {
                errors.put("phone", "Phone must be 7-20 digits, optionally with +, spaces or hyphens.");
            }
        }

        if (client.getNotes() != null && client.getNotes().length() > 500) {
            errors.put("notes", "Notes must be 500 characters or less.");
        }

        if (client.getType() == null) {
            errors.put("type", "Client type is required.");
        }

        if (client.getStatus() == null) {
            errors.put("status", "Client status is required.");
        }

        if (!errors.isEmpty()) {
            throw new com.library.client.domain.exception.ValidationException(errors);
        }
    }
}
