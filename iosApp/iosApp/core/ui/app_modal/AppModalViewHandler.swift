//
//  AppModalViewHandler.swift
//  iosApp
//
//  Created by Bradley Phillips on 4/9/23.
//  Copyright © 2023 orgName. All rights reserved.
//

import SwiftUI
import shared

class AppModalViewHandler: ObservableObject, AppModalViewHandleable
{
    static let shared: AppModalViewHandler = AppModalViewHandler()
    
    var appModalIsShowing = false
    {
        willSet { Task { await MainActor.run { objectWillChange.send() } } }
    }
    
    var appModal: AppModalRepresentable!
    
    init(appModal: AppModalRepresentable? = nil)
    {
        self.appModal = appModal ?? AppModalHelper().appModal()
    }
    
    func setUp() { appModal.setHandler(newHandler: self) }
    
    func onModalShouldHide(animationDuration: Int64)
    {
        appModalIsShowing = false
    }
    
    func onModalShouldShow(animationDuration: Int64)
    {
        appModalIsShowing = true
    }
    
    func modalContent() -> some View
    {
        ZStack
        {
            if let content = appModal.content() as? StatsModal
            {
                StatsModalContent(
                    stats: content.stats().map {
                        StatsModalContent.Stat(
                            headline: $0.headline(),
                            description: $0.description_()
                        )
                    },
                    guessDistribution: StatsGuessDistribution(
                        title: content.guessDistribution().title(),
                        rows: content.guessDistribution().rows().map
                        {
                            StatsGuessDistribution.Row(
                                round: "\($0.round())",
                                correctGuessCount: "\($0.correctGuessesCount())"
                            )
                        }
                    ),
                    playAgainButton: StatsModalContent.PlayAgainButton(
                        label: content.playAgainButton()?.label() ?? "",
                        tapped: { content.playAgainButton()?.click() { _ in } }
                    )
                )
            }
        }
    }
}
