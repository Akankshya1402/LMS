package com.lms.customer.service;

import com.lms.customer.dto.KycDocumentResponse;
import com.lms.customer.dto.KycUploadRequest;

import java.util.List;

public interface KycService {

    void uploadDocument(KycUploadRequest request, String customerId);

    List<KycDocumentResponse> getMyDocuments(String customerId);

    void approveDocument(String documentId, String remarks);

    void rejectDocument(String documentId, String remarks);
}
