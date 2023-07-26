package com.ting.R3S.Projectedsales;

import org.springframework.web.multipart.MultipartFile;

public interface ProjectedsalesService {

    boolean importCsvData(MultipartFile file);

}
