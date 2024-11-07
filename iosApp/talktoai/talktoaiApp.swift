//
//  talkToAIApp.swift
//  talkToAI
//
//  Created by Vladimir Tarasov on 14.04.2024.
//

import SwiftUI
import composeApp

@main
struct talkToAIApp: App {

    init() {
        KoinIOSKt.doInitKoin()
    }

    var body: some Scene {
        WindowGroup {
            ContentView()
        }
    }
}
