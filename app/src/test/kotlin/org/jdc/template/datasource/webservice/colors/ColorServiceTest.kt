package org.jdc.template.datasource.webservice.colors

import assertk.assertThat
import assertk.assertions.isEqualTo
import io.ktor.client.engine.mock.MockEngine
import io.ktor.client.engine.mock.respond
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpStatusCode
import io.ktor.http.headersOf
import io.ktor.utils.io.ByteReadChannel
import kotlinx.coroutines.runBlocking
import org.jdc.template.model.webservice.colors.ColorService
import org.jdc.template.util.ext.ApiResponse
import org.junit.jupiter.api.Test

class ColorServiceTest {
    @Test
    fun getColors() = runBlocking {
        val mockEngine = MockEngine { request ->
            respond(
                content = ByteReadChannel(COLORS_RESPONSE),
                status = HttpStatusCode.OK,
                headers = headersOf(HttpHeaders.ContentType, "application/json")
            )
        }
        val colorService = ColorService(mockEngine)
        val response = colorService.fetchColorsBySafeArgs()
        check(response is ApiResponse.Success)

        val colors = response.value
        assertThat(colors.colors.size).isEqualTo(1)

        val color = colors.colors.first()
        assertThat(color.colorName).isEqualTo("White")
        assertThat(color.hexValue).isEqualTo("#FFFFFF")
    }

    @Test
    fun failedNetwork() = runBlocking {
        val mockEngine = MockEngine { request ->
            respond(
                content = ByteReadChannel("""{"error": "Oh No!" }"""),
                status = HttpStatusCode.InternalServerError,
            )
        }
        val colorService = ColorService(mockEngine)
        val response = colorService.fetchColorsBySafeArgs()
        check(response is ApiResponse.Error)
    }

    companion object {
        const val COLORS_RESPONSE = """
            {
                "colors": [
                    {
                       "colorName": "White",
                       "hexValue": "#FFFFFF"
                    }
                ]
            }
        """
    }
}
