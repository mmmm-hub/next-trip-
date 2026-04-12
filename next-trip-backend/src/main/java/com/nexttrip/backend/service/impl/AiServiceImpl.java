package com.nexttrip.backend.service.impl;

import java.util.List;
import java.util.Locale;
import java.util.regex.Pattern;

import org.springframework.stereotype.Service;

import com.nexttrip.backend.dto.request.AiRequest;
import com.nexttrip.backend.dto.response.AiResponse;
import com.nexttrip.backend.service.AiService;

@Service
public class AiServiceImpl implements AiService {

	/** Évite les faux positifs (ex. 1600) en exigeant que 600 soit un « mot » numérique. */
	private static final Pattern MOCK_BUDGET_600 = Pattern.compile("(^|\\D)600(\\D|$)");

	@Override
	public AiResponse askQuestion(AiRequest request) {
		return buildMockResponse(request.getMessage());
	}

	@Override
	public AiResponse generatePlan(AiRequest request) {
		String msg = request.getMessage();
		if (containsIgnoreCase(msg, "Rome")) {
			return AiResponse.builder()
					.success(true)
					.message("Itinéraire suggéré (mock) pour Rome : jour 1 — Colisée & Forum ; jour 2 — Vatican ; jour 3 — Trastevere.")
					.suggestions(List.of(
							"Réserver le Vatican en ligne",
							"Marcher tôt le matin pour éviter la foule au Colisée"))
					.build();
		}
		return buildMockResponse(msg);
	}

	private static AiResponse buildMockResponse(String raw) {
		String msg = raw != null ? raw : "";
		if (MOCK_BUDGET_600.matcher(msg).find()
				|| containsIgnoreCase(msg, "600€")
				|| containsIgnoreCase(msg, "600 eur")
				|| containsIgnoreCase(msg, "600 euros")) {
			return AiResponse.builder()
					.success(true)
					.message("Avec un budget autour de 600€, nous vous recommandons (mock) un city trip 4–5 jours en Europe du Sud hors haute saison.")
					.suggestions(List.of("Porto", "Séville", "Athènes"))
					.build();
		}
		if (containsIgnoreCase(msg, "octobre")) {
			return AiResponse.builder()
					.success(true)
					.message("Octobre est une excellente fenêtre (mock) : climats doux en Méditerranée et moins de monde qu'en été.")
					.suggestions(List.of("Croatie", "Portugal", "Japon (fin octobre)"))
					.build();
		}
		return AiResponse.builder()
				.success(true)
				.message("Je suis l'assistant Next Trip (mode démo). Posez une question sur une destination, un budget ou une saison, et affinez avec des critères (climat, ambiance, durée).")
				.suggestions(List.of("Budget 1200€ pour 10 jours", "Mer et détente en avril", "Culture et gastronomie"))
				.build();
	}

	private static boolean containsIgnoreCase(String haystack, String needle) {
		return haystack != null && haystack.toLowerCase(Locale.ROOT).contains(needle.toLowerCase(Locale.ROOT));
	}
}
