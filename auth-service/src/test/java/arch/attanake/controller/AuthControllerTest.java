package arch.attanake.controller;

import arch.attanake.dto.JwtAuthenticationDto;
import arch.attanake.dto.RefreshTokenDto;
import arch.attanake.dto.UserCredentialsDto;
import arch.attanake.security.jwt.JwtService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private ObjectMapper objectMapper;

    private static final String CLEAN_UP = "DELETE FROM users;";

    private static final String INSERT_DATA =
            "INSERT INTO users (user_id, first_name, last_name, email, username, password)" +
            " VALUES" +
            " ('550e8400-e29b-41d4-a716-446655440000'," +
            " 'Viktor'," +
            " 'Test'," +
            " 'test@gmail.com'," +
            " 'attanake'," +
            " '$2a$04$xb3AGo43ibESGvux4FoC2.mr2amOsBV0fx9EvT8FeoSfsVFpolYc2');";

    @Test
    @Sql(statements = {CLEAN_UP, INSERT_DATA})
    void singInTest() throws Exception {
        UserCredentialsDto userCredentialsDto = new UserCredentialsDto();
        userCredentialsDto.setUsername("attanake");
        userCredentialsDto.setPassword("12345");

        String userJson = objectMapper.writeValueAsString(userCredentialsDto);

        String tokenJson = mockMvc.perform(MockMvcRequestBuilders.post("/auth/sing-in")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(userJson))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        JwtAuthenticationDto jwtAuthenticationDto = objectMapper.readValue(tokenJson, JwtAuthenticationDto.class);

        Assertions.assertEquals(userCredentialsDto.getUsername(), jwtService.getUsernameFromToken(jwtAuthenticationDto.getToken()));
    }

    @Test
    @Sql(statements  = {CLEAN_UP, INSERT_DATA})
    void singInNegativeTest() throws Exception {
        UserCredentialsDto userCredentialsDto = new UserCredentialsDto();
        userCredentialsDto.setUsername("attanake");
        userCredentialsDto.setPassword("125555");

        String userJson = objectMapper.writeValueAsString(userCredentialsDto);

        mockMvc.perform(MockMvcRequestBuilders.post("/auth/sing-in")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(userJson))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @Sql(statements  = {CLEAN_UP, INSERT_DATA})
    void refresh() throws Exception {
        UserCredentialsDto userCredentialsDto = new UserCredentialsDto();
        userCredentialsDto.setUsername("attanake");
        userCredentialsDto.setPassword("12345");

        String userJson = objectMapper.writeValueAsString(userCredentialsDto);

        String tokenJson = mockMvc.perform(MockMvcRequestBuilders.post("/auth/sing-in")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(userJson))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        RefreshTokenDto refreshTokenDto = new RefreshTokenDto();

        refreshTokenDto.setRefreshToken(objectMapper.readValue(tokenJson, JwtAuthenticationDto.class).getRefreshToken());

        String refreshTokenJson = objectMapper.writeValueAsString(refreshTokenDto);

        mockMvc.perform(MockMvcRequestBuilders.post("/auth/refresh")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(refreshTokenJson))
                .andExpect(status().isOk());
    }
}
