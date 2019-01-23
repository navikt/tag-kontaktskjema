package no.nav.tag.kontaktskjema;

import static no.nav.tag.kontaktskjema.TestData.lagKontaktskjema;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;

import java.time.LocalDateTime;

import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.relational.core.conversion.DbActionExecutionException;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class KontaktskjemaRepositoryTest {

    @Autowired
    private KontaktskjemaRepository kontaktskjemaRepository;

    @Autowired
    private Transactor transactor;

    @After
    public void tearDown() {
        kontaktskjemaRepository.deleteAll();
    }
    
    @Test
    public void skalLagreOgHenteUt() {
        Kontaktskjema lagretSkjema = kontaktskjemaRepository.save(lagKontaktskjema());

        assertThat(kontaktskjemaRepository.findById(lagretSkjema.getId()).isPresent(), is(true));
    }

    @Test(expected=DbActionExecutionException.class)
    public void skalFeileHvisKommuneErForLang() {
        Kontaktskjema kontaktskjema = lagKontaktskjema();
        kontaktskjema.setKommunenr("12345");
        kontaktskjemaRepository.save(kontaktskjema);
    }
    
    @Test
    public void skalHenteBasertPaDato() {
        kontaktskjemaRepository.save(skjemaMedDato(LocalDateTime.now().minusDays(3)));
        kontaktskjemaRepository.save(skjemaMedDato(LocalDateTime.now().minusDays(1)));

        assertThat(kontaktskjemaRepository.findAllNewerThan(LocalDateTime.now().minusDays(4)).size(), is(2));
        assertThat(kontaktskjemaRepository.findAllNewerThan(LocalDateTime.now().minusDays(2)).size(), is(1));
        assertThat(kontaktskjemaRepository.findAllNewerThan(LocalDateTime.now()).size(), is(0));
    }

    private Kontaktskjema skjemaMedDato(LocalDateTime opprettetTidspunkt) {
        Kontaktskjema skjema1 = lagKontaktskjema();
        skjema1.setOpprettet(opprettetTidspunkt);
        return skjema1;
    }

    @Test
    public void skalHenteSkjemaSomIkkeHarGsakOppgave() {
        transactor.inTransaction(() -> {
            kontaktskjemaRepository.save(lagKontaktskjema());
            assertThat(kontaktskjemaRepository.findAllWithNoGsakOppgave().size(), is(1));
        });
    }
}
