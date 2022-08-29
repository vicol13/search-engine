package com.vvv.engine.service

import com.vvv.engine.configuration.SearchServiceTestConfiguration
import com.vvv.engine.domain.*
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.Mockito
import org.mockito.kotlin.times
import org.mockito.kotlin.whenever
import org.powermock.reflect.Whitebox
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.junit.jupiter.SpringExtension

@ExtendWith(SpringExtension::class)
@ContextConfiguration(classes = [SearchServiceTestConfiguration::class])
class SearchServiceTest {

    @Autowired
    private lateinit var preprocessorMock: Preprocessor<IndexedWord>

    @Autowired
    private lateinit var fileServiceMock: FileService

    @Autowired
    private lateinit var searchService: SearchService


    private lateinit var stateMap: MutableMap<String, FileMap>

    @BeforeEach
    fun init() {
        stateMap = mutableMapOf<String, FileMap>(
            "cat" to FileMap()
        )

        stateMap["cat"]!!
            .update("about-cats.txt")
            .update(10, Index(10, 13))
    }

    @Test
    fun `should search for word in file`() {
        //given
        val fileSearchDTOSample = searchResultSample()
        val expectedResult = WordSearchResultDTO("cat", listOf(fileSearchDTOSample))
        Whitebox.setInternalState(searchService, "map", stateMap)

        Mockito.doReturn("cat")
            .whenever(preprocessorMock)
            .toBaseForm("cat")

        Mockito.doReturn(fileSearchDTOSample)
            .whenever(fileServiceMock)
            .get("about-cats.txt", stateMap["cat"]!!.entries().first().value)

        //when
        val result = searchService.search("cat")

        //then
        Assertions.assertEquals(result, expectedResult)
        Mockito.verify(preprocessorMock, times(1))
            .toBaseForm("cat")

        Mockito.verify(fileServiceMock, times(1))
            .get("about-cats.txt", stateMap["cat"]!!.entries().first().value)

    }


    @Test
    fun `should throw an error if the word is not in the map`() {
        //given
        Whitebox.setInternalState(searchService, "map", stateMap)

        //when & then
        Assertions.assertThrows(NoSuchWordException::class.java) {
            searchService.search("dog")
        }
    }

    private fun searchResultSample(): FileSearchDTO {
        return FileSearchDTO(
            "about-cats.txt",
            sentences = listOf(
                SentenceSearchDTO(
                    "This is sentence about cats",
                    line = 10,
                    indexes = listOf(
                        Index(10, 13)
                    )
                )
            )
        )
    }
}