package com.example.lab6secondpart.controller;

import com.example.lab6secondpart.model.Device;
import com.example.lab6secondpart.model.MeasurementConsumption;
import com.example.lab6secondpart.model.enums.MeasuringUnitEnergyConsumption;
import com.example.lab6secondpart.service.DeviceService;
import com.example.lab6secondpart.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@Controller
@RequestMapping("/device")
public class DeviceWebController {

	private final DeviceService deviceService;

	@Autowired
	public DeviceWebController(DeviceService deviceService) {
		this.deviceService = deviceService;
	}

	@GetMapping
	public String showDevices(Model model) {

		model.addAttribute("devices", deviceService.getAllDevices());

		return "devices"; // Thymeleaf template name
	}

	@GetMapping("/measurements/{deviceID}")
	public String showAllMeasurementConsumptions(@PathVariable UUID deviceID, Model model) {

		List<MeasurementConsumption> measurementConsumptionList = deviceService.getAllMeasurementsForDevice(deviceID);
		model.addAttribute("measurementConsumptions", measurementConsumptionList);

		return "measurements"; // Thymeleaf template name
	}

	@GetMapping("/{deviceId}/add-measurement")
	public String showMeasurementForm(@PathVariable UUID deviceId, Model model) {
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
		deviceService.createMeasurement(deviceId, measurementDTO);

		return "redirect:/device"; // Redirect to a suitable endpoint after processing
	}
}
