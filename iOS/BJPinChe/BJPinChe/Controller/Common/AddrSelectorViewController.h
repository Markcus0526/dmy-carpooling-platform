//
//  AddrSelectorViewController.h
//  BJPinChe
//
//  Created by Kimoc on 14-8-23.
//  Copyright (c) 2014年 KimOC. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "AddrSelTableViewCell_new.h"
@protocol AddrSelDelegate;

@interface AddrSelectorViewController : UIViewController <UITableViewDataSource, UITableViewDelegate, UITextFieldDelegate, GlobalSvcDelegate,cellDelegate>
{
    /********************** UI Controls **********************/
    IBOutlet UITableView *              _tableView;
    IBOutlet UITextField *              _txtAddress;
    
    /********************** Member Variable ***********************/
    NSMutableArray *                    mAddrArray;
    NSInteger                           mParentTag;
    NSString *btnHidden;
}

@property(strong, nonatomic) id<AddrSelDelegate> delegate;

@property (nonatomic, readwrite) NSInteger mParentTag;
@property (nonatomic,strong) NSString * defText;
@property (nonatomic, strong)NSString *saveUser;

@property (nonatomic,strong) NSString * city;
@property (nonatomic, weak) IBOutlet UIButton*		btn_remove;
@property (nonatomic, weak) IBOutlet UIImageView*	img_remove;

- (IBAction)onClickedBack:(id)sender;

@end



// address selector protocol
@protocol AddrSelDelegate <NSObject>

- (void) onSelectedAddress : (STBaiduAddrInfo *)addrInfo parentTag:(NSInteger)parentTag;

@end
