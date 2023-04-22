package com.kasperserzysko.dynaapp.controllers;

import com.kasperserzysko.dynaapp.contracts.CurrencyDto;
import com.kasperserzysko.dynaapp.exceptions.BadRequestException;
import com.kasperserzysko.dynaapp.exceptions.NotFoundException;
import com.kasperserzysko.dynaapp.services.NbpService;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PastOrPresent;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.io.IOException;
import java.util.Date;
import java.util.List;

@RestController
@RequiredArgsConstructor
@Validated
public class NbpController {

    private final NbpService nbpService;

    @GetMapping("/rate")
    public ResponseEntity<CurrencyDto> getAvgExchangeRate(
            @RequestParam("date")
            @PastOrPresent(message = "Date can't be future")
            @DateTimeFormat(pattern="yyyy-MM-dd") Date date,
            @RequestParam("code")
            @NotBlank(message = "Currency code is wrong!") String code) throws IOException, NotFoundException, BadRequestException {

        return ResponseEntity.ok(nbpService.getAvgRate(date, code));
    }
    @GetMapping("/minmax")
    public ResponseEntity<String> getMinAndMax(
            @RequestParam("quotations") @Min(value = 0, message = "Quotation can't be lesser than 0") @Max(value = 255, message = "Quotation can't be higher than 255") int quotations,
            @RequestParam("code")
            @NotBlank(message = "Currency code is wrong!") String code) throws IOException, NotFoundException, BadRequestException {

        return ResponseEntity.ok(nbpService.getMaxAndMin(code,quotations));
    }
    @GetMapping("/diff")
    public ResponseEntity<String> getMaxDifference(
            @RequestParam("quotations") @Min(value = 0, message = "Quotation can't be lesser than 0") @Max(value = 255, message = "Quotation can't be higher than 255") int quotations,
            @RequestParam("code")
            @NotBlank(message = "Currency code is wrong!") String code) throws IOException, NotFoundException, BadRequestException {

        return ResponseEntity.ok(nbpService.getBiggestDifference(code,quotations));
    }




    //EXCEPTION HANDLERS
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(ConstraintViolationException.class)
    public List<String> handleValidationExceptions(ConstraintViolationException ex) {
        return ex.getConstraintViolations()
                .stream()
                .map(ConstraintViolation::getMessage).toList();
    }
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(NotFoundException.class)
    public String handleNotFoundException(NotFoundException exception){
        return exception.getMessage();
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(BadRequestException.class)
    public String handleBadRequestException(BadRequestException exception){
        return exception.getMessage();
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public String handleMethodArgumentTypeMismatchException(){
        return "Date format is invalid, try yyyy-MM-dd.";
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(IllegalArgumentException.class)
    public String handleIllegalArgumentException(){
        return "Currency code is wrong!";
    }
}
