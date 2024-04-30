package roomescape.repository;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import roomescape.model.Theme;

@Repository
public class ThemeRepository {

    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert themeInsert;

    public ThemeRepository(final JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.themeInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("theme")
                .usingGeneratedKeyColumns("id");
    }

    public Theme save(final Theme theme) {
        final BeanPropertySqlParameterSource themeParameters = new BeanPropertySqlParameterSource(theme);
        final Long savedThemeId = themeInsert.executeAndReturnKey(themeParameters).longValue();
        return new Theme(savedThemeId, theme.getName(), theme.getDescription(), theme.getThumbnail());
    }
}
