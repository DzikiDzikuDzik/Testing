package pl.zalewski;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;

class SalaryCalculatorServiceTest {

    @Mock private Employee employee;
    @Mock private TaskManagementSystem taskManagementSystem;
    @Mock private SalaryCalculatorService salaryCalculatorService;

    @BeforeEach
    public void init() {
        MockitoAnnotations.openMocks(this);
        Mockito.when(employee.getName()).thenReturn("Employee");
        Mockito.when(employee.getBaseSalary()).thenReturn(BigDecimal.valueOf(5000));
        salaryCalculatorService = new SalaryCalculatorService(taskManagementSystem);
    }

    @Test
    @DisplayName("Correct getting name")
    public void correctGetName() {
        String name = "Anna";
        Mockito.when(employee.getName()).thenReturn("Anna");
        Assertions.assertThat(employee.getName()).isEqualTo(name);
    }

    @Test
    @DisplayName("Correct getting base salary")
    public void correctGetBaseSalary() {
        Assertions.assertThat(BigDecimal.valueOf(5000)).isEqualTo(employee.getBaseSalary());
    }

    @DisplayName("0 Bonus")
    @ParameterizedTest(name = "{index} - 0 bonus for {arguments} tasks")
    @ValueSource(ints = {0, 8, 9})
    public void noBonusForLessThan10Task(int params) {
        Mockito.when(taskManagementSystem.countFinishedTasksForEmployee(employee)).thenReturn(params);
        BigDecimal salaryWithBonus = salaryCalculatorService.calculateSalary(employee);
        Assertions.assertThat(salaryWithBonus).isEqualByComparingTo(employee.getBaseSalary());
    }

    @DisplayName("100 Bonus")
    @ParameterizedTest(name ="{index} - 100 bonus for {arguments} tasks")
    @ValueSource(ints = {10, 11, 19})
    public void bonus100for10tasks(int params) {
        BigDecimal bonus = BigDecimal.valueOf(100d);
        Mockito.when(taskManagementSystem.countFinishedTasksForEmployee(employee)).thenReturn(params);
        BigDecimal salaryWithBonus = salaryCalculatorService.calculateSalary(employee);
        Assertions.assertThat(salaryWithBonus).isEqualByComparingTo(employee.getBaseSalary().add(bonus));
    }

    @ParameterizedTest(name = "{index} - 500 for {arguments} tasks")
    @DisplayName("500 Bonus")
    @ValueSource(ints = {20, 21})
    public void bonus500for20tasks(int params) {
        BigDecimal bonus = BigDecimal.valueOf(500);
        Mockito.when(taskManagementSystem.countFinishedTasksForEmployee(employee)).thenReturn(params);
        BigDecimal salaryWithBonus = salaryCalculatorService.calculateSalary(employee);
        Assertions.assertThat(salaryWithBonus).isEqualByComparingTo(employee.getBaseSalary().add(bonus));
    }
}