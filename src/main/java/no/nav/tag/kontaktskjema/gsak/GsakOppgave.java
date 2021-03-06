package no.nav.tag.kontaktskjema.gsak;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

import org.springframework.data.annotation.Id;

@Data
@Builder
public class GsakOppgave {

    @Id
    private Integer id;
    private Integer kontaktskjemaId;
    private Integer gsakId;
    private OppgaveStatus status;
    private LocalDateTime opprettet;
    
    public enum OppgaveStatus {
        DISABLED,
        OK,
        FEILET
    }
}
