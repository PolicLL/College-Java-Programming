package com.example.demo.converter;

import com.example.demo.model.measurement.MeasuringUnitEnergyConsumption;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

//@Converter(autoApply = true)
//public class MeasuringUnitEnergyConsumptionConverter implements AttributeConverter<MeasuringUnitEnergyConsumption, String> {
//
//	@Override
//	public String convertToDatabaseColumn(MeasuringUnitEnergyConsumption attribute) {
//		return attribute != null ? attribute.name() : null;
//	}
//
//	@Override
//	public MeasuringUnitEnergyConsumption convertToEntityAttribute(String dbData) {
//		return dbData != null ? MeasuringUnitEnergyConsumption.kWh : null;
//	}
//}