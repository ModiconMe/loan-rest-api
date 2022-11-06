package ru.popov.loanrestapi.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.ExceptionHandler;
import ru.popov.loanrestapi.services.LimitService;
import ru.popov.loanrestapi.util.ErrorResponse;
import ru.popov.loanrestapi.util.exceptions.PersonNotFoundException;
import ru.popov.loanrestapi.util.exceptions.RequestLimitException;

import javax.servlet.*;
import java.io.IOException;
import java.util.Locale;
/**
 * Добавлен функционал ограничения кол-ва запросов клиентов из одной страны
 */
@Component
public class LimitFilter implements Filter {

    private final LimitService limitService;
    private final ObjectMapper mapper = new ObjectMapper();

    @Autowired
    public LimitFilter(LimitService limitService) {
        this.limitService = limitService;
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        Locale locale = servletRequest.getLocale();
        if (!limitService.isLimit(locale != null ? locale.getCountry() : "lv")) {
            filterChain.doFilter(servletRequest, servletResponse);
        } else {
//            servletResponse.getOutputStream().write(mapper.writeValueAsBytes(new Error("Exceed execute from this country")));
            throw new RequestLimitException("request limit from country " + locale.getCountry() + " is over");
        }
    }
}
