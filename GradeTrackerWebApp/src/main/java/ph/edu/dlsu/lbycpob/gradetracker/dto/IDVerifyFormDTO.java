package ph.edu.dlsu.lbycpob.gradetracker.dto;

import jakarta.validation.constraints.NotBlank;
// ============================================================
// IDVerifyFormDTO.java
// ============================================================
public class IDVerifyFormDTO {

    @NotBlank(message = "Please enter an ID number.")
    private String idNumber;

    public String getIdNumber()           { return idNumber; }
    public void   setIdNumber(String id)  { this.idNumber = id; }
}

