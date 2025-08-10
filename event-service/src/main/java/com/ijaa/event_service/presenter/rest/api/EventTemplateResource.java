package com.ijaa.event_service.presenter.rest.api;

import com.ijaa.event_service.domain.common.ApiResponse;
import com.ijaa.event_service.domain.request.EventTemplateRequest;
import com.ijaa.event_service.domain.response.EventTemplateResponse;
import com.ijaa.event_service.service.EventTemplateService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/v1/event-templates")
@Tag(name = "Event Templates", description = "Event template management APIs")
public class EventTemplateResource {

    @Autowired
    private EventTemplateService eventTemplateService;

    @GetMapping
    @Operation(summary = "Get all templates", description = "Retrieve all event templates")
    public ResponseEntity<ApiResponse<List<EventTemplateResponse>>> getAllTemplates() {
        List<EventTemplateResponse> templates = eventTemplateService.getAllTemplates();
        ApiResponse<List<EventTemplateResponse>> response = new ApiResponse<>();
        response.setMessage("All templates retrieved successfully");
        response.setCode("SUCCESS");
        response.setData(templates);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/public")
    @Operation(summary = "Get public templates", description = "Retrieve all public event templates")
    public ResponseEntity<ApiResponse<List<EventTemplateResponse>>> getPublicTemplates() {
        List<EventTemplateResponse> templates = eventTemplateService.getPublicTemplates();
        ApiResponse<List<EventTemplateResponse>> response = new ApiResponse<>();
        response.setMessage("Public templates retrieved successfully");
        response.setCode("SUCCESS");
        response.setData(templates);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/user")
    @Operation(summary = "Get user's templates", description = "Retrieve all templates created by the current user")
    public ResponseEntity<ApiResponse<List<EventTemplateResponse>>> getTemplatesByUser(Authentication authentication) {
        String username = authentication.getName();
        List<EventTemplateResponse> templates = eventTemplateService.getTemplatesByUser(username);
        ApiResponse<List<EventTemplateResponse>> response = new ApiResponse<>();
        response.setMessage("User's templates retrieved successfully");
        response.setCode("SUCCESS");
        response.setData(templates);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/available")
    @Operation(summary = "Get available templates", description = "Retrieve templates available to the current user (own + public)")
    public ResponseEntity<ApiResponse<List<EventTemplateResponse>>> getAvailableTemplates(Authentication authentication) {
        String username = authentication.getName();
        List<EventTemplateResponse> templates = eventTemplateService.getAvailableTemplates(username);
        ApiResponse<List<EventTemplateResponse>> response = new ApiResponse<>();
        response.setMessage("Available templates retrieved successfully");
        response.setCode("SUCCESS");
        response.setData(templates);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{templateId}")
    @Operation(summary = "Get template by ID", description = "Retrieve a specific event template by ID")
    public ResponseEntity<ApiResponse<EventTemplateResponse>> getTemplateById(@PathVariable Long templateId) {
        EventTemplateResponse template = eventTemplateService.getTemplateById(templateId);
        ApiResponse<EventTemplateResponse> response = new ApiResponse<>();
        response.setMessage("Template retrieved successfully");
        response.setCode("SUCCESS");
        response.setData(template);
        return ResponseEntity.ok(response);
    }

    @PostMapping
    @Operation(summary = "Create template", description = "Create a new event template")
    public ResponseEntity<ApiResponse<EventTemplateResponse>> createTemplate(
            @Valid @RequestBody EventTemplateRequest request,
            Authentication authentication) {
        String username = authentication.getName();
        EventTemplateResponse template = eventTemplateService.createTemplate(request, username);
        ApiResponse<EventTemplateResponse> response = new ApiResponse<>();
        response.setMessage("Template created successfully");
        response.setCode("SUCCESS");
        response.setData(template);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping("/{templateId}")
    @Operation(summary = "Update template", description = "Update an existing event template")
    public ResponseEntity<ApiResponse<EventTemplateResponse>> updateTemplate(
            @PathVariable Long templateId,
            @Valid @RequestBody EventTemplateRequest request,
            Authentication authentication) {
        String username = authentication.getName();
        EventTemplateResponse template = eventTemplateService.updateTemplate(templateId, request, username);
        ApiResponse<EventTemplateResponse> response = new ApiResponse<>();
        response.setMessage("Template updated successfully");
        response.setCode("SUCCESS");
        response.setData(template);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{templateId}")
    @Operation(summary = "Delete template", description = "Delete an event template")
    public ResponseEntity<ApiResponse<Void>> deleteTemplate(
            @PathVariable Long templateId,
            Authentication authentication) {
        String username = authentication.getName();
        eventTemplateService.deleteTemplateForUser(templateId, username);
        ApiResponse<Void> response = new ApiResponse<>();
        response.setMessage("Template deleted successfully");
        response.setCode("SUCCESS");
        response.setData(null);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/{templateId}/make-public")
    @Operation(summary = "Make template public", description = "Make a template public for all users")
    public ResponseEntity<ApiResponse<EventTemplateResponse>> makeTemplatePublic(@PathVariable Long templateId) {
        EventTemplateResponse template = eventTemplateService.makeTemplatePublic(templateId);
        ApiResponse<EventTemplateResponse> response = new ApiResponse<>();
        response.setMessage("Template made public successfully");
        response.setCode("SUCCESS");
        response.setData(template);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/{templateId}/make-private")
    @Operation(summary = "Make template private", description = "Make a template private")
    public ResponseEntity<ApiResponse<EventTemplateResponse>> makeTemplatePrivate(@PathVariable Long templateId) {
        EventTemplateResponse template = eventTemplateService.makeTemplatePrivate(templateId);
        ApiResponse<EventTemplateResponse> response = new ApiResponse<>();
        response.setMessage("Template made private successfully");
        response.setCode("SUCCESS");
        response.setData(template);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/{templateId}/create-event")
    @Operation(summary = "Create event from template", description = "Create a new event from a template")
    public ResponseEntity<ApiResponse<EventTemplateResponse>> createEventFromTemplate(
            @PathVariable Long templateId,
            @RequestParam LocalDateTime startDate,
            Authentication authentication) {
        String username = authentication.getName();
        EventTemplateResponse template = eventTemplateService.createEventFromTemplate(templateId, startDate, username);
        ApiResponse<EventTemplateResponse> response = new ApiResponse<>();
        response.setMessage("Event created from template successfully");
        response.setCode("SUCCESS");
        response.setData(template);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/search")
    @Operation(summary = "Search templates", description = "Search templates with various criteria")
    public ResponseEntity<ApiResponse<List<EventTemplateResponse>>> searchTemplates(
            @RequestParam(required = false) String templateName,
            @RequestParam(required = false) String title,
            @RequestParam(required = false) String description,
            @RequestParam(required = false) String location,
            @RequestParam(required = false) String eventType,
            @RequestParam(required = false) String organizerName,
            Authentication authentication) {
        
        String username = authentication.getName();
        List<EventTemplateResponse> templates = eventTemplateService.searchTemplates(
                templateName, title, description, location, eventType, organizerName, username);
        ApiResponse<List<EventTemplateResponse>> response = new ApiResponse<>();
        response.setMessage("Template search completed successfully");
        response.setCode("SUCCESS");
        response.setData(templates);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/most-used")
    @Operation(summary = "Get most used templates", description = "Retrieve most used templates")
    public ResponseEntity<ApiResponse<List<EventTemplateResponse>>> getMostUsedTemplates(Authentication authentication) {
        String username = authentication.getName();
        List<EventTemplateResponse> templates = eventTemplateService.getMostUsedTemplates(username);
        ApiResponse<List<EventTemplateResponse>> response = new ApiResponse<>();
        response.setMessage("Most used templates retrieved successfully");
        response.setCode("SUCCESS");
        response.setData(templates);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/user/most-used")
    @Operation(summary = "Get user's most used templates", description = "Retrieve most used templates by the current user")
    public ResponseEntity<ApiResponse<List<EventTemplateResponse>>> getMostUsedTemplatesByUser(Authentication authentication) {
        String username = authentication.getName();
        List<EventTemplateResponse> templates = eventTemplateService.getMostUsedTemplatesByUser(username);
        ApiResponse<List<EventTemplateResponse>> response = new ApiResponse<>();
        response.setMessage("User's most used templates retrieved successfully");
        response.setCode("SUCCESS");
        response.setData(templates);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/event-type/{eventType}")
    @Operation(summary = "Get templates by event type", description = "Retrieve templates by event type")
    public ResponseEntity<ApiResponse<List<EventTemplateResponse>>> getTemplatesByEventType(@PathVariable String eventType) {
        List<EventTemplateResponse> templates = eventTemplateService.getTemplatesByEventType(eventType);
        ApiResponse<List<EventTemplateResponse>> response = new ApiResponse<>();
        response.setMessage("Templates by event type retrieved successfully");
        response.setCode("SUCCESS");
        response.setData(templates);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/recurring")
    @Operation(summary = "Get recurring templates", description = "Retrieve all recurring templates")
    public ResponseEntity<ApiResponse<List<EventTemplateResponse>>> getRecurringTemplates() {
        List<EventTemplateResponse> templates = eventTemplateService.getRecurringTemplates();
        ApiResponse<List<EventTemplateResponse>> response = new ApiResponse<>();
        response.setMessage("Recurring templates retrieved successfully");
        response.setCode("SUCCESS");
        response.setData(templates);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/user/recurring")
    @Operation(summary = "Get user's recurring templates", description = "Retrieve recurring templates by the current user")
    public ResponseEntity<ApiResponse<List<EventTemplateResponse>>> getRecurringTemplatesByUser(Authentication authentication) {
        String username = authentication.getName();
        List<EventTemplateResponse> templates = eventTemplateService.getRecurringTemplatesByUser(username);
        ApiResponse<List<EventTemplateResponse>> response = new ApiResponse<>();
        response.setMessage("User's recurring templates retrieved successfully");
        response.setCode("SUCCESS");
        response.setData(templates);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/{templateId}/duplicate")
    @Operation(summary = "Duplicate template", description = "Duplicate an existing template")
    public ResponseEntity<ApiResponse<EventTemplateResponse>> duplicateTemplate(
            @PathVariable Long templateId,
            @RequestParam String newName,
            Authentication authentication) {
        String username = authentication.getName();
        EventTemplateResponse template = eventTemplateService.duplicateTemplate(templateId, newName, username);
        ApiResponse<EventTemplateResponse> response = new ApiResponse<>();
        response.setMessage("Template duplicated successfully");
        response.setCode("SUCCESS");
        response.setData(template);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/{templateId}/export")
    @Operation(summary = "Export template", description = "Export template in various formats")
    public ResponseEntity<ApiResponse<String>> exportTemplate(
            @PathVariable Long templateId,
            @RequestParam(defaultValue = "json") String format) {
        String exportedData = eventTemplateService.exportTemplate(templateId, format);
        ApiResponse<String> response = new ApiResponse<>();
        response.setMessage("Template exported successfully");
        response.setCode("SUCCESS");
        response.setData(exportedData);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/import")
    @Operation(summary = "Import template", description = "Import template from external source")
    public ResponseEntity<ApiResponse<EventTemplateResponse>> importTemplate(
            @RequestParam String templateData,
            @RequestParam(defaultValue = "json") String format,
            Authentication authentication) {
        String username = authentication.getName();
        EventTemplateResponse template = eventTemplateService.importTemplate(templateData, format, username);
        ApiResponse<EventTemplateResponse> response = new ApiResponse<>();
        response.setMessage("Template imported successfully");
        response.setCode("SUCCESS");
        response.setData(template);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
} 