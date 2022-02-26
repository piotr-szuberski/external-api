package com.pet.project.external.api.pets

import com.pet.project.external.api.config.JacksonConfiguration
import org.springframework.http.MediaType
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultMatchers
import org.springframework.test.web.servlet.setup.MockMvcBuilders
import spock.lang.Specification

import java.time.LocalDateTime

class PetControllerTest extends Specification {

  static path = '/api/v1/pet'

  static validKind = Kind.CAT.name()
  static validId = 'abc'
  static pastDate = LocalDateTime.of(2022, 2, 20, 4, 23, 0)
  static futureDate = pastDate.withYear(3100)

  MockMvc mvc

  def setup() {
    def mapper = new JacksonConfiguration().objectMapper()
    mvc = MockMvcBuilders.standaloneSetup(new PetController())
            .setMessageConverters(new MappingJackson2HttpMessageConverter(mapper))
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
            .andExpect(MockMvcResultMatchers.status().isBadRequest())
            .andReturn().getResponse().getContentAsString()

    then:
    fields.each { it in response }

    where:
    kind      | name    | birthDate  | caregiver | fields
    'invalid' | 'Tofik' | pastDate   | 'id'      | ['id']
    validKind | '   '   | pastDate   | 'id'      | ['kind']
    validKind | 'Tofik' | futureDate | 'id'      | ['name']
    validKind | 'Tofik' | pastDate   | '  '      | ['caregiver']
    validKind | 'Tofik' | futureDate | '  '      | ['caregiver', 'birthDate']
  }

  def "Should receive ok response"() {
    expect:
    mvc.perform(MockMvcRequestBuilders.put("$path/$validId")
            .contentType(MediaType.APPLICATION_JSON)
            .content("""{
                "name": "Tofik",
                "kind": "$validKind",
                "birthDate": "$pastDate",
                "caregiver": $caregiver
            }"""))
            .andExpect(MockMvcResultMatchers.status().isOk())

    where:
    caregiver << ['"id"', null]
  }
}
