//
//  AppSheetViewHandler.swift
//  iosApp
//
//  Created by Bradley Phillips on 5/1/23.
//  Copyright © 2023 orgName. All rights reserved.
//

import SwiftUI
import shared

class AppSheetViewHandler: AppSheetViewHandleable, ObservableObject
{
    static let shared = AppSheetViewHandler()
    
    var appSheet: AppSheetRepresentable!
    var appSheetIsShowing: Bool = false
    {
        willSet { Task { await MainActor.run { objectWillChange.send() } } }
    }
    
    init(appSheet: AppSheetRepresentable? = nil)
    {
        self.appSheet = appSheet ?? AppSheetHelper().appSheet()
    }
    
    func setUp()
    {
        appSheet.setHandler(newHandler_: self)
    }
    
    func onSheetShouldHide(animationDuration: Int64)
    {
        appSheetIsShowing = false
    }
    
    func onSheetShouldShow(animationDuration: Int64)
    {
        appSheetIsShowing = true
    }
    
    func sheetContent() -> some View
    {
        ZStack
        {
            if let content = appSheet.content() as? HelpSheet
            {
                HelpSheetContent(
                    title: content.title(),
                    instructions: content.instructions().map()
                    { instruction in
                        HelpSheetContent.Instruction(
                            instruction: instruction.instruction()
                        )
                    },
                    examples: content.examples().map()
                    { example in
                        HelpSheetContent.Example(
                            tiles: example.tiles().map()
                            { tile in
                                HelpSheetContent.Tile(
                                    letter: "\(tile.letter())",
                                    state: tile.state()
                                )
                            },
                            description: example.description_()
                        )
                    },
                    footer: content.footer(),
                    closeButton: HelpSheetContent.CloseButton()
                )
            }
        }
    }
}
