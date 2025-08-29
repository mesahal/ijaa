package com.ijaa.user.service;

import com.ijaa.user.domain.dto.FeatureFlagDto;
import com.ijaa.user.domain.entity.FeatureFlag;
import com.ijaa.user.domain.converter.FeatureFlagConverter;
import com.ijaa.user.repository.FeatureFlagRepository;
import com.ijaa.user.service.impl.FeatureFlagServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class FeatureFlagServiceTest {

    @Mock
    private FeatureFlagRepository featureFlagRepository;

    @Mock
    private FeatureFlagConverter featureFlagConverter;

    @InjectMocks
    private FeatureFlagServiceImpl featureFlagService;

    private FeatureFlag parentFlag;
    private FeatureFlag childFlag;
    private FeatureFlagDto parentDto;
    private FeatureFlagDto childDto;

    @BeforeEach
    void setUp() {
        // Create parent flag
        parentFlag = new FeatureFlag();
        parentFlag.setId(1L);
        parentFlag.setName("chat");
        parentFlag.setDisplayName("Chat Feature");
        parentFlag.setDescription("Real-time chat functionality");
        parentFlag.setEnabled(true);
        parentFlag.setCreatedAt(LocalDateTime.now());
        parentFlag.setUpdatedAt(LocalDateTime.now());

        // Create child flag
        childFlag = new FeatureFlag();
        childFlag.setId(2L);
        childFlag.setName("chat.file-sharing");
        childFlag.setDisplayName("File Sharing in Chat");
        childFlag.setDescription("Allow file sharing in chat");
        childFlag.setEnabled(true);
        childFlag.setParent(parentFlag);
        childFlag.setCreatedAt(LocalDateTime.now());
        childFlag.setUpdatedAt(LocalDateTime.now());

        // Create DTOs
        parentDto = new FeatureFlagDto();
        parentDto.setName("chat");
        parentDto.setDisplayName("Chat Feature");
        parentDto.setDescription("Real-time chat functionality");
        parentDto.setEnabled(true);

        childDto = new FeatureFlagDto();
        childDto.setName("chat.file-sharing");
        childDto.setDisplayName("File Sharing in Chat");
        childDto.setDescription("Allow file sharing in chat");
        childDto.setEnabled(true);
        childDto.setParentId(1L);
    }

    @Test
    void testIsEnabled_WhenFlagExistsAndEnabled_ReturnsTrue() {
        // Given
        when(featureFlagRepository.findByName("chat")).thenReturn(Optional.of(parentFlag));

        // When
        boolean result = featureFlagService.isEnabled("chat");

        // Then
        assertTrue(result);
        verify(featureFlagRepository).findByName("chat");
    }

    @Test
    void testIsEnabled_WhenFlagExistsAndDisabled_ReturnsFalse() {
        // Given
        parentFlag.setEnabled(false);
        when(featureFlagRepository.findByName("chat")).thenReturn(Optional.of(parentFlag));

        // When
        boolean result = featureFlagService.isEnabled("chat");

        // Then
        assertFalse(result);
    }

    @Test
    void testIsEnabled_WhenFlagDoesNotExist_ReturnsFalse() {
        // Given
        when(featureFlagRepository.findByName("nonexistent")).thenReturn(Optional.empty());

        // When
        boolean result = featureFlagService.isEnabled("nonexistent");

        // Then
        assertFalse(result);
    }

    @Test
    void testIsEnabled_WhenChildFlagAndParentDisabled_ReturnsFalse() {
        // Given
        parentFlag.setEnabled(false);
        when(featureFlagRepository.findByName("chat.file-sharing")).thenReturn(Optional.of(childFlag));
        when(featureFlagRepository.findByName("chat")).thenReturn(Optional.of(parentFlag));

        // When
        boolean result = featureFlagService.isEnabled("chat.file-sharing");

        // Then
        assertFalse(result);
    }

    @Test
    void testIsEnabled_WhenChildFlagAndParentEnabled_ReturnsChildValue() {
        // Given
        parentFlag.setEnabled(true);
        childFlag.setEnabled(false);
        when(featureFlagRepository.findByName("chat.file-sharing")).thenReturn(Optional.of(childFlag));
        when(featureFlagRepository.findByName("chat")).thenReturn(Optional.of(parentFlag));

        // When
        boolean result = featureFlagService.isEnabled("chat.file-sharing");

        // Then
        assertFalse(result);
    }

    @Test
    void testCreateFlag_WhenValidDto_ReturnsCreatedFlag() {
        // Given
        when(featureFlagRepository.existsByName("chat")).thenReturn(false);
        when(featureFlagRepository.save(any(FeatureFlag.class))).thenReturn(parentFlag);

        // When
        FeatureFlag result = featureFlagService.createFlag(parentDto);

        // Then
        assertNotNull(result);
        assertEquals("chat", result.getName());
        assertEquals("Chat Feature", result.getDisplayName());
        assertTrue(result.getEnabled());
        verify(featureFlagRepository).existsByName("chat");
        verify(featureFlagRepository).save(any(FeatureFlag.class));
    }

    @Test
    void testCreateFlag_WhenFlagAlreadyExists_ThrowsException() {
        // Given
        when(featureFlagRepository.existsByName("chat")).thenReturn(true);

        // When & Then
        assertThrows(RuntimeException.class, () -> featureFlagService.createFlag(parentDto));
        verify(featureFlagRepository).existsByName("chat");
        verify(featureFlagRepository, never()).save(any(FeatureFlag.class));
    }

    @Test
    void testCreateFlag_WhenWithParent_ReturnsCreatedFlagWithParent() {
        // Given
        when(featureFlagRepository.existsByName("chat.file-sharing")).thenReturn(false);
        when(featureFlagRepository.findById(1L)).thenReturn(Optional.of(parentFlag));
        when(featureFlagRepository.save(any(FeatureFlag.class))).thenReturn(childFlag);

        // When
        FeatureFlag result = featureFlagService.createFlag(childDto);

        // Then
        assertNotNull(result);
        assertEquals("chat.file-sharing", result.getName());
        assertEquals(parentFlag, result.getParent());
        verify(featureFlagRepository).findById(1L);
    }

    @Test
    void testCreateFlag_WhenParentNotFound_ThrowsException() {
        // Given
        when(featureFlagRepository.existsByName("chat.file-sharing")).thenReturn(false);
        when(featureFlagRepository.findById(1L)).thenReturn(Optional.empty());

        // When & Then
        assertThrows(RuntimeException.class, () -> featureFlagService.createFlag(childDto));
        verify(featureFlagRepository).findById(1L);
        verify(featureFlagRepository, never()).save(any(FeatureFlag.class));
    }

    @Test
    void testUpdateFlag_WhenFlagExists_ReturnsUpdatedFlag() {
        // Given
        when(featureFlagRepository.findByName("chat")).thenReturn(Optional.of(parentFlag));
        when(featureFlagRepository.save(any(FeatureFlag.class))).thenReturn(parentFlag);

        // When
        FeatureFlag result = featureFlagService.updateFlag("chat", false);

        // Then
        assertNotNull(result);
        verify(featureFlagRepository).findByName("chat");
        verify(featureFlagRepository).save(any(FeatureFlag.class));
    }

    @Test
    void testUpdateFlag_WhenFlagDoesNotExist_ThrowsException() {
        // Given
        when(featureFlagRepository.findByName("nonexistent")).thenReturn(Optional.empty());

        // When & Then
        assertThrows(RuntimeException.class, () -> featureFlagService.updateFlag("nonexistent", true));
        verify(featureFlagRepository).findByName("nonexistent");
        verify(featureFlagRepository, never()).save(any(FeatureFlag.class));
    }

    @Test
    void testDeleteFeatureFlag_WhenFlagExists_DeletesFlag() {
        // Given
        when(featureFlagRepository.findByName("chat")).thenReturn(Optional.of(parentFlag));

        // When
        featureFlagService.deleteFeatureFlag("chat");

        // Then
        verify(featureFlagRepository).findByName("chat");
        verify(featureFlagRepository).delete(parentFlag);
    }

    @Test
    void testDeleteFeatureFlag_WhenFlagDoesNotExist_ThrowsException() {
        // Given
        when(featureFlagRepository.findByName("nonexistent")).thenReturn(Optional.empty());

        // When & Then
        assertThrows(RuntimeException.class, () -> featureFlagService.deleteFeatureFlag("nonexistent"));
        verify(featureFlagRepository).findByName("nonexistent");
        verify(featureFlagRepository, never()).delete(any(FeatureFlag.class));
    }

    @Test
    void testGetAllFlags_ReturnsHierarchicalStructure() {
        // Given
        List<FeatureFlag> topLevelFlags = Arrays.asList(parentFlag);
        when(featureFlagRepository.findAllTopLevelFlags()).thenReturn(topLevelFlags);
        when(featureFlagConverter.toDtoWithChildren(parentFlag)).thenReturn(parentDto);

        // When
        List<FeatureFlagDto> result = featureFlagService.getAllFlags();

        // Then
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("chat", result.get(0).getName());
        verify(featureFlagRepository).findAllTopLevelFlags();
        verify(featureFlagConverter).toDtoWithChildren(parentFlag);
    }






}


