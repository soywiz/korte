package com.soywiz.korte.block

import com.soywiz.korio.async.asyncFun
import com.soywiz.korte.Block
import com.soywiz.korte.Template

data class BlockGroup(val children: List<Block>) : Block {
	override suspend fun eval(context: Template.EvalContext) = asyncFun<Unit> { for (n in children) n.eval(context) }
}
