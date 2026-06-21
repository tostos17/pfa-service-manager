package com.pfa.app.model;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "events_generic")
@DiscriminatorValue("STATE_CHANGE")
@Getter
@Setter
public class MatchStateEvent extends MatchEvent {
    private String stateName; // e.g., MATCH_STARTED, HALFTIME, MATCH_ENDED
}