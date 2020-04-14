package com.github.tmurakami.kcps.idea.extensions

import com.github.tmurakami.kcps.compiler.extensions.StorageComponentContainerContributorImpl
import com.github.tmurakami.kcps.idea.compilerPluginIntroduced
import org.jetbrains.kotlin.extensions.StorageComponentContainerContributor

class IdeaStorageComponentContainerContributor :
    StorageComponentContainerContributor by StorageComponentContainerContributorImpl(::compilerPluginIntroduced)
