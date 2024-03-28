package org.joel3112.componentbuilder.actions.components

import com.intellij.openapi.project.Project
import com.intellij.openapi.ui.DialogWrapper
import com.intellij.openapi.ui.ValidationInfo
import com.intellij.ui.DocumentAdapter
import com.intellij.ui.components.JBTextField
import com.intellij.ui.dsl.builder.Cell
import com.intellij.ui.dsl.builder.panel
import com.intellij.ui.dsl.gridLayout.HorizontalAlign
import org.joel3112.componentbuilder.BuilderBundle.message
import javax.swing.JComponent


class CreateDialog(private val project: Project) : DialogWrapper(project) {
    var isCanceled = false
        protected set

    private val nameRegex = "[a-zA-Z0-9]+".toRegex()
    private lateinit var textField: Cell<JBTextField>


    private val createPanel = panel {
        row(message("builder.popup.create.name.label")) {
            textField = textField()
                .horizontalAlign(HorizontalAlign.FILL)
                .focused()
                .validation {
                    if (it.text.isEmpty()) {
                        ValidationInfo(message("builder.popup.create.name.validation.empty"))
                    } else if (!it.text.matches(nameRegex)) {
                        ValidationInfo(message("builder.popup.create.name.validation.specialCharacters"))
                    } else {
                        null
                    }
                }
                .applyToComponent {
                    document.addDocumentListener(object : DocumentAdapter() {
                        override fun textChanged(e: javax.swing.event.DocumentEvent) {
                            okAction.isEnabled = text.isNotEmpty() && text.matches(nameRegex)
                        }
                    })

                }

        }
    }

    init {
        super.init()
        isResizable = false
        title = message("builder.popup.create.title")
    }

    fun getCName(): String {
        return textField.component.text
    }

    override fun createCenterPanel(): JComponent {
        return createPanel
    }

    override fun doOKAction() {
        isCanceled = false

        super.doOKAction()
    }

    override fun doCancelAction() {
        isCanceled = true
        super.doCancelAction()
    }
}
