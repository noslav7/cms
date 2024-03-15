package ru.aston.crm.cms.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.aston.crm.cms.model.Interaction;
import ru.aston.crm.cms.service.InteractionService;

import java.util.List;

@RestController
@RequestMapping("/cms/interactions")
@Tag(name = "InteractionController",
        description = "Controller for particular interactions with customers")
public class InteractionController {
    private final InteractionService interactionService;

    @Autowired
    public InteractionController(InteractionService interactionService) {
        this.interactionService = interactionService;
    }

    @Operation(summary = "Adds new interaction",
            description = "Provide all necessary fields for creating new interaction!",
            responses = {
            @ApiResponse(description = "Interaction created", responseCode = "201",
                    content = @Content(schema = @Schema(implementation = Interaction.class))),
            @ApiResponse(description = "Invalid input", responseCode = "400")
    })
    @PostMapping
    public ResponseEntity<Interaction> createInteraction(
            @Parameter(description = "Interaction object to be stored in a database table", required = true)
            @RequestBody Interaction interaction) {
        Interaction savedInteraction = interactionService.save(interaction);
        return new ResponseEntity<>(savedInteraction, HttpStatus.CREATED);
    }

    @Operation(
            summary = "Get interaction by Id",
            responses = {
                    @ApiResponse(description = "Interaction retrieved", responseCode = "201",
                            content = @Content(schema = @Schema(implementation = Interaction.class))),
                    @ApiResponse(description = "Invalid input", responseCode = "400")
            })
    @GetMapping("/{id}")
    public ResponseEntity<Interaction> getInteractionById(
            @Parameter(description = "Interaction ID to retrieve", required = true) @PathVariable int id) {
        Interaction interaction = interactionService.findById(id);
        return ResponseEntity.ok(interaction);
    }

    @Operation(
            summary = "Get Interaction objects by customer Id",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Successfully retrieved the list of Interactions",
                            content = @Content(array = @ArraySchema(schema = @Schema(implementation = Interaction.class)))),
                    @ApiResponse(description = "Invalid input", responseCode = "400")
            })
    @GetMapping("/{customer_id}")
    public ResponseEntity<List<Interaction>> getByCustomerId(
            @Parameter(description = "Customer ID", required = true)
            @PathVariable int customer_id) {
        List<Interaction> interactions = interactionService.findByCustomerId(customer_id);
        return ResponseEntity.ok(interactions);
    }

    @Operation(
            summary = "Get all Interaction objects",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Successfully retrieved the list of all Interactions",
                            content = @Content(array = @ArraySchema(schema = @Schema(implementation = Interaction.class)))),
                    @ApiResponse(description = "Invalid input", responseCode = "400")
            })
    @GetMapping
    public ResponseEntity<List<Interaction>> getAllContacts() {
        List<Interaction> interactions = interactionService.findAll();
        return ResponseEntity.ok(interactions);
    }

    @Operation(
            summary = "Updates an existing Interaction by Id",
            responses = {
                    @ApiResponse(description = "Interaction updated", responseCode = "201",
                            content = @Content(schema = @Schema(implementation = Interaction.class))),
                    @ApiResponse(description = "Invalid input", responseCode = "400")
            })
    @PutMapping("/{id}")
    public ResponseEntity<Interaction> updateInteraction(
            @Parameter(description = "ID of an Interaction object to update", required = true) @PathVariable int id,
            @Parameter(description = "Updated interaction", required = true) @RequestBody Interaction interaction) {
        Interaction updatedInteraction = interactionService.update(id, interaction);
        return ResponseEntity.ok(updatedInteraction);
    }

    @Operation(summary = "Deletes an existing Interaction by Id")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteInteraction(
            @Parameter(description = "ID of an Interaction to be deleted", required = true) @PathVariable int id) {
        interactionService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
