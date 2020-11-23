package com.example.repro

import org.jetbrains.kotlin.backend.common.extensions.IrGenerationExtension
import org.jetbrains.kotlin.backend.common.extensions.IrPluginContext
import org.jetbrains.kotlin.ir.ObsoleteDescriptorBasedAPI
import org.jetbrains.kotlin.ir.declarations.IrModuleFragment
import org.jetbrains.kotlin.ir.types.IrSimpleType
import org.jetbrains.kotlin.ir.types.classifierOrFail
import org.jetbrains.kotlin.ir.util.render
import org.jetbrains.kotlin.name.FqName
import org.jetbrains.kotlin.resolve.descriptorUtil.fqNameSafe

class MyIrGenerationExtension : IrGenerationExtension {
    @OptIn(ObsoleteDescriptorBasedAPI::class)
    override fun generate(moduleFragment: IrModuleFragment, pluginContext: IrPluginContext) {
        val myFunction = pluginContext.referenceFunctions(
            FqName("com.example.a.myFunction")
        ).single().owner

        val myFunctionReturnType = ((myFunction.returnType as? IrSimpleType)
            ?.abbreviation?.typeAlias?.descriptor?.fqNameSafe ?: myFunction.returnType.classifierOrFail.descriptor.fqNameSafe)
            .asString()

        check(myFunctionReturnType == "com.example.a.MySuspendTypeAlias") {
            "Expected 'com.example.a.MySuspendTypeAlias' but was '${myFunction.returnType.render()}'"
        }
    }
}