package com.m2.msisdn.test.controller;

import com.m2.msisdn.test.model.Msisdn;
import com.m2.msisdn.test.model.Response;
import com.m2.msisdn.test.service.MsisdnService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/v1/m2/msisdn")
public class MsisdnController {

    @Autowired
    private MsisdnService msisdnService;

    @GetMapping
    public List<Msisdn> getAll() {
        return msisdnService.getAll();
    }

    @GetMapping("/{msisdn}")
    public Msisdn getByMsisdn(@PathVariable String msisdn) {
        return msisdnService.getByMsisdn(msisdn);
    }

    @PostMapping
    public ResponseEntity<Response> uploadSingleCSVFile(@RequestParam("csvfile") MultipartFile csvfile) {
        if (csvfile.getOriginalFilename().isEmpty()) {
            final Response response = new Response();
            response.setErrorMessage("No selected file to upload! Please do the checking");
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
        try {
            final Response response = msisdnService.processCsvFile(csvfile.getInputStream());
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (IOException e) {
            final Response response = new Response();
            response.setErrorMessage("Invalid input file.");
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/download/output/")
    public void downloadOutputFile(HttpServletResponse response) throws IOException {
        response.setContentType("text/plain");
        response.setHeader("Content-Disposition", "attachment; filename=MSISDN.txt");
        msisdnService.loadFile(response.getWriter());
    }

    @GetMapping("/download/rejected/")
    public void downloadRejectedFile(HttpServletResponse response) throws IOException {
        response.setContentType("text/plain");
        response.setHeader("Content-Disposition", "attachment; filename=MSISDN_REJECTED.txt");
        msisdnService.loadRejectedFile(response.getWriter());
    }
}




