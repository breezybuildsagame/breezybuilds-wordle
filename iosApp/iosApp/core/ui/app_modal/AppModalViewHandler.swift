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
                    stats: [],
                    guessDistribution: StatsGuessDistribution(
                        title: "",
                        rows: []
                    ),
                    playAgainButton: StatsModalContent.PlayAgainButton(
                        label: "",
                        tapped: { }
                    )
                )
            }
        }
    }
}
