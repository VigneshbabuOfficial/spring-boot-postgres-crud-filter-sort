package com.school.dto;

import java.time.LocalDate;
import java.util.Optional;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import com.school.utils.NotBlankForOptional;
import com.school.utils.ValidRequestBody;

@ValidRequestBody(groups = {Update.class})
public class StudentsDTO {

	private Long id;

	@NotBlankForOptional(message = "Sorry! Please enter valid name.",groups = {Create.class})
	private Optional<@NotNull(message = "Sorry! name must not be null", groups = { 
			Update.class }) @NotBlank(message = "Sorry! name must not be blank", groups = { 
					Update.class }) String> name;
	
	private Optional<@NotNull(message = "Sorry! Date Of Birth must not be null", groups = { 
			Update.class } ) LocalDate> dateOfBirth;

	@NotBlankForOptional(message="Sorry! Please enter valid address.", groups = Create.class)
	private Optional<@NotNull(message = "Sorry! address must not be null", groups = { 
			Update.class }) @NotBlank(message = "Sorry! address must not be blank", groups = { 
					Update.class })String> address;

	@NotBlankForOptional(message="Sorry! Please enter valid gender.", groups = Create.class)
	private Optional<@NotNull(message = "Sorry! gender must not be null", groups = { 
			Update.class }) @NotBlank(message = "Sorry! gender must not be blank", groups = { 
					Update.class })String> gender;

	@NotBlankForOptional(groups = Create.class)
	private Optional<@NotNull(message = "contactNumber must not be null", groups = { 
			Update.class }) Long> contactNumber;

	@NotBlankForOptional(message="Sorry! Please enter valid sports.",groups = Create.class)
	private Optional<String> sports;

	@NotBlankForOptional(message="Sorry! Please enter valid curriculums.",groups = Create.class)
	private Optional<String> curriculums;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Optional<String> getName() {
		return name;
	}

	public void setName(Optional<String> name) {
		this.name = name;
	}

	public Optional<LocalDate> getDateOfBirth() {
		return dateOfBirth;
	}

	public void setDateOfBirth(Optional<LocalDate> dateOfBirth) {
		this.dateOfBirth = dateOfBirth;
	}

	public Optional<String> getAddress() {
		return address;
	}

	public void setAddress(Optional<String> address) {
		this.address = address;
	}

	public Optional<String> getGender() {
		return gender;
	}

	public void setGender(Optional<String> gender) {
		this.gender = gender;
	}

	public Optional<Long> getContactNumber() {
		return contactNumber;
	}

	public void setContactNumber(Optional<Long> contactNumber) {
		this.contactNumber = contactNumber;
	}


	public Optional<String> getSports() {
		return sports;
	}

	public void setSports(Optional<String> sports) {
		this.sports = sports;
	}

	public Optional<String> getCurriculums() {
		return curriculums;
	}

	public void setCurriculums(Optional<String> curriculums) {
		this.curriculums = curriculums;
	}

	@Override
	public String toString() {
		return "StudentsDTO [id=" + id + ", name=" + name + ", dateOfBirth=" + dateOfBirth + ", address=" + address
				+ ", gender=" + gender + ", contactNumber=" + contactNumber + ", sports=" + sports + ", curriculums="
				+ curriculums + "]";
	}

}
