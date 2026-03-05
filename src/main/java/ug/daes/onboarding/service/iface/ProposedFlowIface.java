package ug.daes.onboarding.service.iface;

import org.springframework.web.multipart.MultipartFile;
import ug.daes.onboarding.constant.ApiResponse;
import ug.daes.onboarding.dto.TemporaryTableDTO;
import ug.daes.onboarding.dto.UpdateTemporaryTableDto;

public interface ProposedFlowIface {

    public ApiResponse saveDataTemporyTable(TemporaryTableDTO temporaryTableDTO);

    public ApiResponse submitObData(String idDocumentNumber);

    ApiResponse updateRecord(UpdateTemporaryTableDto updateTemporaryTableDto);


    ApiResponse deleteRecord(UpdateTemporaryTableDto updateTemporaryTableDto);

    ApiResponse saveStep2Details(TemporaryTableDTO temporaryTableDTO, MultipartFile livelinessVideo,String selfie);

    ApiResponse getAllSubscriberExtractFeatures();
    
    
    ApiResponse encriptedString(TemporaryTableDTO temporaryTableDTO);

    void deleteOldRecords();
    


}
