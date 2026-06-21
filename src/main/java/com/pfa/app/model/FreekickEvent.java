package com.pfa.app.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "events_freekick")
@DiscriminatorValue("FREEKICK")
@Getter
@Setter
public class FreekickEvent extends MatchEvent {
    private boolean dangerousPosition;
    @ManyToOne(fetch = FetchType.LAZY)
    private Player taker;
}