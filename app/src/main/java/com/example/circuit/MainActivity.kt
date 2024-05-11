// Copyright (C) 2022 Slack Technologies, LLC
// SPDX-License-Identifier: Apache-2.0
package com.example.circuit

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.circuit.ui.theme.CircuitTheme
import com.slack.circuit.foundation.Circuit
import com.slack.circuit.foundation.CircuitCompositionLocals
import com.slack.circuit.foundation.CircuitContent
import com.slack.circuit.runtime.presenter.Presenter
import com.slack.circuit.runtime.ui.Ui
import com.slack.circuit.runtime.ui.ui
import com.example.circuit.repository.IngredientsRepositoryImpl
import com.example.circuit.step.FillingsProducerImpl
import com.example.circuit.step.ToppingsProducerImpl
import com.example.circuit.step.confirmationProducer
import com.example.circuit.step.summaryProducer

class  MainActivity : ComponentActivity() {
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    enableEdgeToEdge()

    val circuit: Circuit =
      Circuit.Builder()
        .addPresenterFactory(buildPresenterFactory())
        .addUiFactory(buildUiFactory())
        .build()

    setContent {
      CircuitTheme {
        CircuitCompositionLocals(circuit) {
        CircuitContent(OrderTacosScreen)
      } }
    }
  }
}

private fun buildPresenterFactory(): Presenter.Factory =
  Presenter.Factory { _, _, _ ->
    OrderTacosPresenter(
      fillingsProducer = FillingsProducerImpl(IngredientsRepositoryImpl),
      toppingsProducer = ToppingsProducerImpl(IngredientsRepositoryImpl),
      confirmationProducer = { details, _ -> confirmationProducer(details) },
      summaryProducer = { _, sink -> summaryProducer(sink) },
    )
  }

private fun buildUiFactory(): Ui.Factory =
  Ui.Factory { _, _ ->
    ui<OrderTacosScreen.State> { state, modifier -> OrderTacosUi(state, modifier) }
  }
