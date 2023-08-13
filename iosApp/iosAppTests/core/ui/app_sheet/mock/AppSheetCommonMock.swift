//
//  AppSheetCommonMock.swift
//  iosAppTests
//
//  Created by Bradley Phillips on 5/1/23.
//  Copyright © 2023 orgName. All rights reserved.
//

import Foundation
import shared

class AppSheetCommonMock: AppSheetRepresentable
{
    var contentToReturn: AppSheetContentRepresentable? = HelpSheet(
        title: "Help",
        instructions: [
            HelpSheet.Instruction(instruction: "Mock instruction")
        ],
        examples: [
            HelpSheet.Example(
                description: "Mock description..",
                tiles: [
                    HelpSheet.Tile(
                        letter: "G".utf16.first!,
                        state: HelpSheet.TileState.hidden
                    )
                ]
            )
        ],
        footer: "Mock Footer.."
    )
    var handlerToReturn: AppSheetViewHandleable? = nil
    var setContentPassedInNewContent: AppSheetContentRepresentable? = nil
    var setHandlerPassedInNewHandler: AppSheetViewHandleable? = nil
    
    func content() -> AppSheetContentRepresentable?
    {
        self.contentToReturn
    }
    
    func handler() -> AppSheetViewHandleable?
    {
        self.handlerToReturn
    }
    
    func setContent(newContent_ newContent: AppSheetContentRepresentable?)
    {
        self.setContentPassedInNewContent = newContent
    }
    
    func setHandler(newHandler_ newHandler: AppSheetViewHandleable?)
    {
        self.setHandlerPassedInNewHandler = newHandler
    }
}
