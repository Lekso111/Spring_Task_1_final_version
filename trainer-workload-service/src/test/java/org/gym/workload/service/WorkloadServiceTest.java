package org.gym.workload.service;

import org.gym.workload.dto.ActionType;
import org.gym.workload.dto.TrainerSummaryResponse;
import org.gym.workload.dto.TrainerWorkloadRequest;
import org.gym.workload.model.Month;
import org.gym.workload.model.TrainerWorkload;
import org.gym.workload.model.Year;
import org.gym.workload.repository.TrainerWorkloadRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class WorkloadServiceTest {

    private TrainerWorkloadRepository repository;
    private WorkloadService service;

    @BeforeEach
    void setUp() {
        repository = mock(TrainerWorkloadRepository.class);
        service = new WorkloadService(repository);
    }

    @Test
    void updateWorkloadCreatesNewDocumentWhenTrainerDoesNotExist() {
        when(repository.findByUsername("john.doe")).thenReturn(Optional.empty());
        TrainerWorkloadRequest request = new TrainerWorkloadRequest(
                "john.doe", "John", "Doe", true,
                LocalDate.of(2024, 5, 10), 3.5, ActionType.ADD);

        service.updateWorkload(request);

        ArgumentCaptor<TrainerWorkload> captor = ArgumentCaptor.forClass(TrainerWorkload.class);
        verify(repository).save(captor.capture());
        TrainerWorkload saved = captor.getValue();

        assertThat(saved.getUsername()).isEqualTo("john.doe");
        assertThat(saved.getFirstName()).isEqualTo("John");
        assertThat(saved.getLastName()).isEqualTo("Doe");
        assertThat(saved.isActive()).isTrue();
        assertThat(saved.getYears()).hasSize(1);

        Year year = saved.getYears().get(0);
        assertThat(year.getYear()).isEqualTo(2024);
        assertThat(year.getMonths()).hasSize(1);

        Month month = year.getMonths().get(0);
        assertThat(month.getMonth()).isEqualTo(5);
        assertThat(month.getSummaryDuration()).isEqualTo(3.5);
    }

    @Test
    void updateWorkloadAddsDurationToExistingSummary() {
        TrainerWorkload existing = new TrainerWorkload("john.doe", "John", "Doe", true);
        existing.addDuration(2024, 5, 2.0);
        when(repository.findByUsername("john.doe")).thenReturn(Optional.of(existing));
        TrainerWorkloadRequest request = new TrainerWorkloadRequest(
                "john.doe", "John", "Doe", true,
                LocalDate.of(2024, 5, 10), 4.0, ActionType.ADD);

        service.updateWorkload(request);

        ArgumentCaptor<TrainerWorkload> captor = ArgumentCaptor.forClass(TrainerWorkload.class);
        verify(repository).save(captor.capture());
        TrainerWorkload saved = captor.getValue();

        double duration = saved.getYears().get(0).getMonths().get(0).getSummaryDuration();
        assertThat(duration).isEqualTo(6.0);
    }

    @Test
    void updateWorkloadSubtractsDurationOnDeleteAction() {
        TrainerWorkload existing = new TrainerWorkload("john.doe", "John", "Doe", true);
        existing.addDuration(2024, 5, 10.0);
        when(repository.findByUsername("john.doe")).thenReturn(Optional.of(existing));
        TrainerWorkloadRequest request = new TrainerWorkloadRequest(
                "john.doe", "John", "Doe", true,
                LocalDate.of(2024, 5, 10), 4.0, ActionType.DELETE);

        service.updateWorkload(request);

        ArgumentCaptor<TrainerWorkload> captor = ArgumentCaptor.forClass(TrainerWorkload.class);
        verify(repository).save(captor.capture());
        double duration = captor.getValue().getYears().get(0).getMonths().get(0).getSummaryDuration();
        assertThat(duration).isEqualTo(6.0);
    }

    @Test
    void updateWorkloadNeverGoesBelowZero() {
        TrainerWorkload existing = new TrainerWorkload("john.doe", "John", "Doe", true);
        existing.addDuration(2024, 5, 2.0);
        when(repository.findByUsername("john.doe")).thenReturn(Optional.of(existing));
        TrainerWorkloadRequest request = new TrainerWorkloadRequest(
                "john.doe", "John", "Doe", true,
                LocalDate.of(2024, 5, 10), 5.0, ActionType.DELETE);

        service.updateWorkload(request);

        ArgumentCaptor<TrainerWorkload> captor = ArgumentCaptor.forClass(TrainerWorkload.class);
        verify(repository).save(captor.capture());
        double duration = captor.getValue().getYears().get(0).getMonths().get(0).getSummaryDuration();
        assertThat(duration).isEqualTo(0.0);
    }

    @Test
    void getSummaryReturnsEmptyResponseWhenTrainerMissing() {
        when(repository.findByUsername("missing")).thenReturn(Optional.empty());

        TrainerSummaryResponse response = service.getSummary("missing");

        assertThat(response.username()).isEqualTo("missing");
        assertThat(response.firstName()).isNull();
        assertThat(response.lastName()).isNull();
        assertThat(response.status()).isFalse();
        assertThat(response.years()).isEmpty();
        verify(repository, never()).save(any());
    }

    @Test
    void getSummaryReturnsSortedYearsAndMonths() {
        TrainerWorkload existing = new TrainerWorkload("john.doe", "John", "Doe", true);
        existing.addDuration(2024, 5, 3.0);
        existing.addDuration(2023, 12, 1.0);
        existing.addDuration(2024, 1, 2.0);
        when(repository.findByUsername("john.doe")).thenReturn(Optional.of(existing));

        TrainerSummaryResponse response = service.getSummary("john.doe");

        assertThat(response.years()).extracting("year").containsExactly(2023, 2024);
        assertThat(response.years().get(1).months()).extracting("month").containsExactly(1, 5);
    }

    @Test
    void updateWorkloadRefreshesTrainerMetadata() {
        TrainerWorkload existing = new TrainerWorkload("john.doe", "OldFirst", "OldLast", false);
        when(repository.findByUsername("john.doe")).thenReturn(Optional.of(existing));
        TrainerWorkloadRequest request = new TrainerWorkloadRequest(
                "john.doe", "John", "Doe", true,
                LocalDate.of(2024, 5, 10), 1.0, ActionType.ADD);

        service.updateWorkload(request);

        ArgumentCaptor<TrainerWorkload> captor = ArgumentCaptor.forClass(TrainerWorkload.class);
        verify(repository).save(captor.capture());
        TrainerWorkload saved = captor.getValue();
        assertThat(saved.getFirstName()).isEqualTo("John");
        assertThat(saved.getLastName()).isEqualTo("Doe");
        assertThat(saved.isActive()).isTrue();
    }

    @Test
    void findByFirstNameAndLastNameDelegatesToRepository() {
        TrainerWorkload existing = new TrainerWorkload("john.doe", "John", "Doe", true);
        when(repository.findByFirstNameAndLastName("John", "Doe")).thenReturn(List.of(existing));

        List<TrainerWorkload> result = repository.findByFirstNameAndLastName("John", "Doe");

        assertThat(result).containsExactly(existing);
        verify(repository).findByFirstNameAndLastName(eq("John"), eq("Doe"));
    }
}
