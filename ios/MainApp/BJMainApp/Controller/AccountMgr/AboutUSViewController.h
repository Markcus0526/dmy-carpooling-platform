//
//  AboutUSViewController.h
//  BJMainApp
//
//  Created by KimOC on 7/26/14.
//  Copyright (c) 2014 KimOC. All rights reserved.
//

#import <UIKit/UIKit.h>

@interface AboutUSViewController : UIViewController
{
    /********************** UI Controls **********************/
    IBOutlet UIImageView *          _imgMain;
    IBOutlet UILabel *              _lblVersion;
}

- (IBAction)onClickedBack:(id)sender;
- (IBAction)onClickedPhone:(id)sender;

@end
