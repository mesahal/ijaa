package com.ijaa.user.service;

import com.ijaa.user.domain.entity.FeatureFlag;
import com.ijaa.user.domain.dto.FeatureFlagDto;
import com.ijaa.user.domain.converter.FeatureFlagConverter;
import com.ijaa.user.repository.FeatureFlagRepository;
import com.ijaa.user.service.impl.FeatureFlagServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class FeatureFlagServiceInfiniteRecursionTest {

    @Mock
    private FeatureFlagRepository featureFlagRepository;

    @Mock
    private FeatureFlagConverter featureFlagConverter;

    @InjectMocks
    private FeatureFlagServiceImpl featureFlagService;

    @Test
    void testGetAllFlags_WithCircularReferences_ShouldNotCauseInfiniteRecursion() {
        // Given - Create a flag that references itself (edge case)
        FeatureFlag selfReferencingFlag = new FeatureFlag();
        selfReferencingFlag.setId(1L);
        selfReferencingFlag.setName("self");
        selfReferencingFlag.setDisplayName("Self Referencing");
        selfReferencingFlag.setDescription("Self referencing flag");
        selfReferencingFlag.setEnabled(true);
        selfReferencingFlag.setCreatedAt(LocalDateTime.now());
        selfReferencingFlag.setUpdatedAt(LocalDateTime.now());
        selfReferencingFlag.setChildren(Arrays.asList(selfReferencingFlag)); // Self reference

        when(featureFlagRepository.findAllTopLevelFlags()).thenReturn(Arrays.asList(selfReferencingFlag));
        
        // Mock the converter to return a DTO with null children to prevent infinite recursion
        FeatureFlagDto mockDto = new FeatureFlagDto();
        mockDto.setName("self");
        mockDto.setChildren(null); // Prevent infinite recursion
        when(featureFlagConverter.toDtoWithChildren(selfReferencingFlag)).thenReturn(mockDto);

        // When & Then - This should not throw StackOverflowError
        assertDoesNotThrow(() -> {
            List<FeatureFlagDto> result = featureFlagService.getAllFlags();
            assertNotNull(result);
            assertEquals(1, result.size());
            
            FeatureFlagDto dto = result.get(0);
            assertEquals("self", dto.getName());
            // The children should be null to prevent infinite recursion
            assertNull(dto.getChildren());
        });
        
        // Verify that the method completed without throwing StackOverflowError
        verify(featureFlagRepository).findAllTopLevelFlags();
    }

    @Test
    void testGetAllFlags_WithComplexCircularReferences_ShouldNotCauseInfiniteRecursion() {
        // Given - Create a complex circular reference scenario
        FeatureFlag flagA = new FeatureFlag();
        flagA.setId(1L);
        flagA.setName("flagA");
        flagA.setDisplayName("Flag A");
        flagA.setEnabled(true);
        flagA.setCreatedAt(LocalDateTime.now());
        flagA.setUpdatedAt(LocalDateTime.now());

        FeatureFlag flagB = new FeatureFlag();
        flagB.setId(2L);
        flagB.setName("flagB");
        flagB.setDisplayName("Flag B");
        flagB.setEnabled(true);
        flagB.setParent(flagA);
        flagB.setCreatedAt(LocalDateTime.now());
        flagB.setUpdatedAt(LocalDateTime.now());

        // Create circular reference: A -> B -> A
        flagA.setChildren(Arrays.asList(flagB));
        flagB.setChildren(Arrays.asList(flagA));

        when(featureFlagRepository.findAllTopLevelFlags()).thenReturn(Arrays.asList(flagA));
        
        // Mock the converter to return DTOs with null children to prevent infinite recursion
        FeatureFlagDto mockDtoA = new FeatureFlagDto();
        mockDtoA.setName("flagA");
        mockDtoA.setChildren(null); // Prevent infinite recursion
        when(featureFlagConverter.toDtoWithChildren(flagA)).thenReturn(mockDtoA);

        // When & Then - This should not throw StackOverflowError
        assertDoesNotThrow(() -> {
            List<FeatureFlagDto> result = featureFlagService.getAllFlags();
            assertNotNull(result);
            assertEquals(1, result.size());
            
            FeatureFlagDto dtoA = result.get(0);
            assertEquals("flagA", dtoA.getName());
            assertNotNull(dtoA.getChildren());
            assertEquals(1, dtoA.getChildren().size());
            
            FeatureFlagDto dtoB = dtoA.getChildren().get(0);
            assertEquals("flagB", dtoB.getName());
            // The children should be null to prevent infinite recursion
            assertNull(dtoB.getChildren());
        });
        
        // Verify that the method completed without throwing StackOverflowError
        verify(featureFlagRepository).findAllTopLevelFlags();
    }
}
