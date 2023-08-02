//
//  HelpSheetContentInstructionTests.swift
//  iosAppTests
//
//  Created by Bradley Phillips on 8/2/23.
//  Copyright © 2023 orgName. All rights reserved.
//

import XCTest
import SwiftUI
import ViewInspector
@testable import iosApp

final class HelpSheetContentInstructionTests: XCTestCase
{
    func test_when_Instruction_appears__view_matches_design_requirements() throws
    {
        // given
        let instructionText = "Guess the **WORDLE** in 6 tries."
        let sut = HelpSheetContent.Instruction(instruction: instructionText)
        let dimensions = SceneDimensions()
        let expectedFontSize = mockFrame().height * (15.0 / idealFrame().height)
        let expectedBottomPadding = mockFrame().height * (20.0 / idealFrame().height)
        
        // when
        defer { ViewHosting.expel() }
        ViewHosting.host(view: sut.environmentObject(dimensions))
        dimensions.setDimensions(width: mockFrame().width, height: mockFrame().height)
        let textAttributes = try sut.inspect().find(text: instructionText).attributes()
        
        // then
        XCTAssertEqual("Roboto-Regular", try textAttributes.font().name())
        XCTAssertEqual(expectedFontSize, try textAttributes.font().size())
        XCTAssertEqual(Color.ui.onSurface, try textAttributes.foregroundColor())
        XCTAssertEqual(expectedBottomPadding, try sut.inspect().view(HelpSheetContent.Instruction.self).text().padding(.bottom))
    }
}
