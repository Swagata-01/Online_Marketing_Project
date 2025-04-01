package com.cts.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Data
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name="products")
public class Products {
	@Id
	@Column(name="productid")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int productid;
	
	@Column
	private String name;
	
	@Column
	private String description;
	
	@Lob
	@Column(columnDefinition = "LONGBLOB")
	private byte[] images;
	
	@Column(name="isactive")
	private Boolean isActive = true;
	
	@Column(name="addedon")
	private LocalDateTime addedOn = LocalDateTime.now();
	
	@Column(name="updatedon")
	private LocalDateTime updatedOn = LocalDateTime.now();
}
