//
//  Drv_PassengerInfoViewController.h
//  BJPinChe
//
//  Created by Kim Ok Chol on 8/27/14.
//  Copyright (c) 2014 KimOC. All rights reserved.
//

#import <UIKit/UIKit.h>

@interface Drv_PassEvalCell : UITableViewCell

@property (nonatomic, strong) IBOutlet UILabel * _lblName;
@property (nonatomic, strong) IBOutlet UILabel * _lblEvalState;
@property (nonatomic, strong) IBOutlet UILabel * _lblContents;
@property (nonatomic, strong) IBOutlet UILabel * _lblTime;

@end

@interface Drv_PassengerInfoViewController : SuperViewController <UITableViewDataSource, UITableViewDelegate, OrderSvcDelegate>
{
    /********************** UI Controls **********************/
    IBOutlet UIImageView *              _imgUser;
    IBOutlet UIImageView *              _imgSex;
    IBOutlet UILabel *                  _lblAge;
    IBOutlet UILabel *                  _lblName;
    IBOutlet UILabel *                  _lblVerifDesc;
    IBOutlet UILabel *                  _lblEvalRate;
    IBOutlet UILabel *                  _lblCount;
    IBOutlet UIButton *                 _markVerif;
    
    IBOutlet UITableView *              _tblEval;
    
    /********************** Member Variable ***********************/
    STPassengerInfo *                   mPassengerInfo;
    NSMutableArray *                    mEvalArray;
    int                                 mCurEvalPageNo;
    long                                mLastEvalId;
    
    int                                 mCurPageNO;
    
    NSDictionary *falsePassDic;
    
}

@property (nonatomic, readwrite) long           mPassID;
@property (nonatomic, retain) NSString *startTimer;

- (IBAction)onClickedBack:(id)sender;

@end
