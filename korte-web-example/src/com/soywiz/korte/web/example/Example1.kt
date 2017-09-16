package com.soywiz.korte.web.example

import com.soywiz.korio.async.Korio
import com.soywiz.korio.ext.web.router.*
import com.soywiz.korio.inject.AsyncInjector
import com.soywiz.korio.net.http.Http
import com.soywiz.korio.net.http.HttpServer
import com.soywiz.korio.net.http.createHttpServer
import com.soywiz.korio.vfs.ResourcesVfs
import com.soywiz.korio.vfs.VfsFile
import com.soywiz.korte.Templates

fun main(args: Array<String>) = Korio {
	val templates = Templates(ResourcesVfs)
	val injector = AsyncInjector()
			.map(templates)
	val server = createHttpServer()
			.router(KorRouter(injector)
					.registerRoutes<RootRoute>()
			)
			.listen(8080)
	println("Listening at... ${server.actualPort}")
}

@Suppress("unused")
class RootRoute(
		val templates: Templates
) {
	val staticRoot = ResourcesVfs["static"].jail()

	@Route(Http.Methods.GET, "/")
	suspend fun root(): String {
		return templates.get("index.html").invoke(hashMapOf<String, Any?>(

		))
	}

	@Route(Http.Methods.GET, "/*", priority = RoutePriority.LOWEST)
	suspend fun static(req: HttpServer.Request): VfsFile {
		return staticRoot[req.uri]
	}

	@Route(Http.Methods.GET, "/demo")
	suspend fun demo(): String {
		return "DEMO!"
	}

}