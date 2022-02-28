package com.pet.project.external.api.pets

import com.fasterxml.jackson.databind.ObjectMapper
import com.pet.project.external.api.config.JacksonConfiguration
import com.pet.project.external.api.interceptors.ExceptionHandlerAdvice
import org.springframework.http.MediaType
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.setup.MockMvcBuilders
import spock.lang.Shared
import spock.lang.Specification

import java.time.LocalDateTime

class PetControllerTest extends Specification {

  static path = '/api/v1/pets'

  static validKind = Kind.CAT.name()
  static validId = 'abc'
  static pastDate = LocalDateTime.of(2022, 2, 20, 4, 23, 0)
  static futureDate = pastDate.withYear(3100)

  MockMvc mvc

  @Shared
  static ObjectMapper mapper = new JacksonConfiguration().objectMapper()

  def setup() {
    mvc = MockMvcBuilders.standaloneSetup(new PetController())
            .setMessageConverters(new MappingJackson2HttpMessageConverter(mapper))
            .setControllerAdvice(new ExceptionHandlerAdvice())
            .build()
  }

  def "Should receive error response"() {
    when:
    def response = mvc.perform(MockMvcRequestBuilders.put("$path/$validId")
            .contentType(MediaType.APPLICATION_JSON)
            .content("""{
                "name": "$name",
                "kind": "$kind",
                "birthDate": "$birthDate",
                "caregiver": "$caregiver"
            }"""))
            .andReturn()
            .getResponse()

    then:
    response.status == 400

    where:
    kind      | name    | birthDate  | caregiver
    'invalid' | 'Tofik' | pastDate   | 'id'
    validKind | '   '   | pastDate   | 'id'
    validKind | 'Tofik' | futureDate | 'id'
    validKind | 'Tofik' | pastDate   | '  '
    validKind | 'Tofik' | futureDate | '  '
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
            .andReturn()
            .getResponse()

    then:
    response.status == 200

    where:
    caregiver << ['"id"', null]
  }
}
