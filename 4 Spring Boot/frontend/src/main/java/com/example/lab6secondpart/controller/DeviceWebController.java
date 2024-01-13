package com.example.lab6secondpart.controller;

import com.example.lab6secondpart.model.MeasurementConsumption;
import com.example.lab6secondpart.model.enums.MeasuringUnitEnergyConsumption;
import com.example.lab6secondpart.service.DeviceService;
import com.example.lab6secondpart.utils.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;


@Controller
@RequestMapping("/device")
public class DeviceWebController {

	private final Logger logger = LoggerFactory.getLogger(DeviceWebController.class);

	private final DeviceService deviceService;

	@Autowired
	public DeviceWebController(DeviceService deviceService) {

		this.deviceService = deviceService;
	}

	@GetMapping
	public String showDevices(Model model) {

		logger.info("Fetching all devices");
		model.addAttribute("devices", deviceService.getAllDevices());
		return "devices"; // Thymeleaf template name
	}

	@GetMapping("/measurements/{deviceID}")
	public String showAllMeasurementConsumptions(@PathVariable UUID deviceID, Model model) {

		logger.info("Fetching all measurements for device with ID: {}", deviceID);
		List<MeasurementConsumption> measurementConsumptionList = deviceService.getAllMeasurementsForDevice(deviceID);
		model.addAttribute("measurementConsumptions", measurementConsumptionList);
		return "measurements"; // Thymeleaf template name
	}

	@GetMapping("/{deviceId}/add-measurement")
	public String showMeasurementForm(@PathVariable UUID deviceId, Model model) {

		logger.info("Displaying measurement form for device with ID: {}", deviceId);
		model.addAttribute("deviceId", deviceId);
		model.addAttribute("measuringUnits", MeasuringUnitEnergyConsumption.values());
		model.addAttribute("measurementDTO", new MeasurementConsumption());
		return "measurement-form"; // Thymeleaf template name for measurement form
	}

	@PostMapping("/{deviceId}/save-measurement")
	public String saveMeasurement(
			@PathVariable UUID deviceId,
			@ModelAttribute MeasurementConsumption measurementDTO
	) {

		logger.info("Saving measurement for device with ID: {} and values {}", deviceId, measurementDTO);
		deviceService.createMeasurement(deviceId, measurementDTO);
		return "redirect:/device"; // Redirect to a suitable endpoint after processing
	}

}
