package dev.thiagooliveira.deepstrike.application.port.outbound;

import dev.thiagooliveira.deepstrike.application.dto.GameSummary;
import dev.thiagooliveira.deepstrike.domain.PlayerId;
import java.util.List;

public interface GameSummaryRepository {

  List<GameSummary> findAll(PlayerId playerId);
}
