package com.pet.project.external.api.pets

import com.fasterxml.jackson.databind.ObjectMapper
import com.pet.project.external.api.config.JacksonConfiguration
import com.pet.project.external.api.error.ErrorCode
import com.pet.project.external.api.error.ErrorDto
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultHandlers
import spock.lang.Shared
import spock.lang.Specification
import spock.lang.Unroll

import java.time.LocalDateTime

@WebMvcTest(controllers = [PetController])
class PetControllerIT extends Specification {

  static path = '/api/v1/pets'

  static validKind = Kind.CAT.name()
  static validId = 'abc'
  static pastDate = LocalDateTime.of(2022, 2, 20, 4, 23, 0)
  static futureDate = pastDate.withYear(3100)

  @Autowired
  MockMvc mvc

  @Autowired
  static ObjectMapper mapper = new JacksonConfiguration().objectMapper()

  @Unroll
  def "Should receive error response for invalid fields #fields"() {
    when:
    def response = mvc.perform(MockMvcRequestBuilders.put("$path/$validId")
            .contentType(MediaType.APPLICATION_JSON)
            .content("""{
                "name": "$name",
                "kind": "$kind",
                "birthDate": "$birthDate",
                "caregiver": "$caregiver"
            }"""))
            .andDo(MockMvcResultHandlers.print())
            .andReturn()
            .getResponse()

    then:
    response.status == 400

    and:
    def errorDto = mapper.readValue(response.getContentAsString(), ErrorDto)
    errorDto.errors.size() == fields.size()
    errorDto.code == ErrorCode.INPUT_VALIDATION_ERROR
    fields.each { response.getContentAsString().contains(it) }

    where:
    kind      | name    | birthDate | caregiver | fields
    'invalid' | 'Tofik' | pastDate  | 'id'      | ['kind']
    validKind | '   '   | pastDate   | 'id'      | ['kind']
    validKind | 'Tofik' | futureDate | 'id'      | ['name']
    validKind | 'Tofik' | pastDate   | '  '      | ['caregiver']
    validKind | 'Tofik' | futureDate | '  '      | ['caregiver', 'birthDate']
  }

  def "Should receive ok response"() {
    when:
    def response = mvc.perform(MockMvcRequestBuilders.put("$path/$validId")
            .contentType(MediaType.APPLICATION_JSON)
            .content("""{
                "name": "Tofik",
                "kind": "$validKind",
                "birthDate": "$pastDate",
                "caregiver": $caregiver
            }"""))
            .andDo(MockMvcResultHandlers.print())
            .andReturn()
            .getResponse()

    then:
    response.status == 200

    where:
    caregiver << ['"id"', null]
  }
}
