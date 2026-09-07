package com.library.stock.infrastructure.web;

import java.util.Map;

public interface EditionNamesProvider {

    Map<Long, String> getEditionNames(String status);
}
